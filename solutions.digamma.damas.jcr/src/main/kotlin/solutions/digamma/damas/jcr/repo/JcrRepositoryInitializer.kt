package solutions.digamma.damas.jcr.repo

import solutions.digamma.damas.config.Configuration
import solutions.digamma.damas.config.Configurations
import solutions.digamma.damas.config.Fallback
import solutions.digamma.damas.jcr.login.UserLoginModule
import solutions.digamma.damas.jcr.repo.job.NamespaceDeclaration
import solutions.digamma.damas.jcr.repo.job.NodeCreation
import solutions.digamma.damas.jcr.repo.job.NodeTypeDeclaration
import solutions.digamma.damas.jcr.repo.job.RepositoryJob
import solutions.digamma.damas.jcr.sys.SystemSessions
import java.net.URI
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import java.util.logging.Level
import java.util.logging.Logger
import javax.enterprise.inject.Instance
import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.Node
import javax.jcr.Repository
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.security.AccessControlList

/**
 * Repository initializer.
 *
 * A best effort is applied when initializing repository. That means that errors
 * are ignored, and unless the error is fatal, the rest convert the current job,
 * as well as remaining jobs, are still attempted.
 *
 * @author Ahmad Shahwan
 */
@Singleton
internal class JcrRepositoryInitializer : RepositoryInitializer {

    /**
     * Lock used for thread safety.
     * @see "http://stackoverflow.com/a/1040821/3402449"
     */
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()
    private var isReady = false

    @Inject
    private lateinit var jobs: Instance<RepositoryJob>

    @Inject
    private lateinit var logger: Logger

    @Inject
    private lateinit var nodeTypesInitializer: NodeTypesRegistrar

    @Inject
    @Configuration(RepositoryJob.SKIP_INIT_ALL_CONF_KEY)
    @Fallback(Configurations.FALSE)
    var skipAllInit: Boolean = false

    @Inject
    @Configuration(RepositoryJob.SKIP_INIT_NS_CONF_KEY)
    @Fallback(Configurations.FALSE)
    var skipNamespaceInit: Boolean = false

    @Inject
    @Configuration(RepositoryJob.SKIP_INIT_NT_CONF_KEY)
    @Fallback(Configurations.FALSE)
    var skipNodeTypeInit: Boolean = false

    @Inject
    @Configuration(RepositoryJob.SKIP_INIT_CREATE_CONF_KEY)
    @Fallback(Configurations.FALSE)
    var skipCreationInit: Boolean = false

    /**
     * Prepare repository for use.
     *
     * @param repository The repository to initialize.
     */
    override fun initialize(repository: Repository) {
        if (this.skipAllInit || (
                    this.skipNamespaceInit &&
                    this.skipNodeTypeInit &&
                    this.skipCreationInit)
        ) {
            this.logger.info(
                "Initializing JCR repository skipped as per configurations.")
            return
        }
        this.logger.info("Initializing JCR repository.")
        /* Use constructor, since SystemSessions is not available for
         * injection at this point.
         */
        val system = SystemSessions(repository)
        var superuser: Session? = null
        try {
            superuser = system.superuser
            if (!this.skipNamespaceInit) {
                this.logger.info("Initializing repository namespaces.")
                this.jobs.flatMap { it.namespaces }
                    .forEach { this.registerNamespace(it, superuser) }
            } else {
                this.logger.info("Repository namespaces init skipped.")
            }
            if (!this.skipNodeTypeInit) {
                this.logger.info("Initializing repository node types.")
                this.jobs
                    .map { it.types }
                    .forEach { this.registerNodeTypes(it, superuser) }
            } else {
                this.logger.info("Repository node type init skipped.")
            }
            if (!this.skipCreationInit) {
                this.logger.info("Initializing repository nodes.")
                this.jobs
                    .flatMap { it.nodes }
                    .forEach { this.createNode(it, superuser) }
            } else {
                this.logger.info("Repository nodes init skipped.")
            }
        } catch (e: RepositoryException) {
            this.logger.log(
                    Level.SEVERE, "Repo job unable to connect as admin.", e)
        } finally {
            try {
                if (superuser != null) {
                    superuser.save()
                    this.logger.info("Init session saved.")
                }
            } catch (e: RepositoryException) {
                this.logger.log(
                        Level.SEVERE, "Repo job unable to save session.", e)
            }
        }
        /* Set system session in user-login module. */
        UserLoginModule.system = system
        /* Signal readiness. */
        this.lock.lock()
        try {
            this.isReady = true
            condition.signalAll()
        } finally {
            this.lock.unlock()
        }
    }

    private fun registerNamespace(
        namespace: NamespaceDeclaration,
        session: Session) {
        try {
            val registry = session.workspace.namespaceRegistry
            if (!registry.prefixes.contains(namespace.prefix)) {
                registry.registerNamespace(namespace.prefix, namespace.url)
                this.logger.info { "Namespace ${namespace.prefix} registered." }
            }
        } catch (e: RepositoryException) {
            throw IllegalStateException(
                "Unable to acquire namespace registry", e)
        }
    }

    private fun registerNodeTypes(
        types: List<NodeTypeDeclaration>,
        session: Session) {
        this.nodeTypesInitializer.registerNodeTypes(types, session)
    }

    private fun createNode(node: NodeCreation, session: Session) {
        try {
            val jcrRoot = session.rootNode
            val path = URI
                .create(jcrRoot.path)
                .relativize(URI.create(node.path))
                .path
            val jcrNode: Node
            if (jcrRoot.hasNode(path)) {
                jcrNode = jcrRoot.getNode(path)
                this.logger.info { "Node ${node.path} already exists." }
            } else {
                jcrNode = jcrRoot.addNode(path, node.type)
                this.logger.info { "Node ${jcrNode.path} added." }
            }
            for (mixin in node.mixins) {
                try {
                    jcrNode.addMixin(mixin)
                } catch (e: RepositoryException) {
                    this.logger.warning {
                        "Repo job unable to add mixin $mixin to ${node.path}."
                    }
                }
            }
            for ((subject, rights) in node.accessRights) {
                val policy = try {
                    session.accessControlManager
                        .getPolicies(jcrNode.path)
                        .asSequence()
                        .filterIsInstance<AccessControlList>()
                        .first()
                } catch (e: NoSuchElementException) {
                    continue
                }
                val acm = jcrNode.session.accessControlManager
                policy.accessControlEntries
                    .filter { it.principal.name == subject }
                    .forEach { policy.removeAccessControlEntry(it) }
                val privileges = rights
                    .map { acm.privilegeFromName(it) }
                    .toTypedArray()
                policy.addAccessControlEntry({ subject }, privileges)
                acm.setPolicy(jcrNode.path, policy)
            }
        } catch (e: RepositoryException) {
            this.logger.severe { "Repo job error adding node ${node.path}." }
        }
    }

    @Throws(InterruptedException::class)
    override fun waitUntilDone() {
        this.lock.lock()
        try {
            while (!this.isReady) {
                condition.await()
            }
        } finally {
            this.lock.unlock()
        }
    }

    @Throws(InterruptedException::class)
    override fun waitUntilDone(timeout: Long): Boolean {
        this.lock.lock()
        try {
            if (!this.isReady) {
                condition.await(timeout, TimeUnit.SECONDS)
            }
            return this.isReady
        } finally {
            this.lock.unlock()
        }
    }
}
