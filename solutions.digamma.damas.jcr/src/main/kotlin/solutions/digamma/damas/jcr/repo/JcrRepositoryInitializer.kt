package solutions.digamma.damas.jcr.repo

import java.net.URI
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.Node
import javax.jcr.Repository
import javax.jcr.RepositoryException
import javax.jcr.Session

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
    private val jobs: MutableList<RepositoryJob> = ArrayList(1)

    @Inject
    private lateinit var logger: Logger

    /**
     * Prepare repository for use.
     *
     * @param repository The repository to initialize.
     */
    override fun initialize(repository: Repository) {
        this.logger.info("Initializing JCR repository.")
        /* Use constructor, since SystemRepository is not available for
         * injection at this point.
         */
        val system = SystemRepository(repository)
        this.collectJobs()
        this.logger.info { "%d jobs collected".format(this.jobs.size) }
        var superuser: Session? = null
        try {
            superuser = system.superuserSession
            for (job in this.jobs) {
                for (node in job.creations) {
                    try {
                        val jcrRoot = superuser.rootNode
                        val path = URI
                                .create(jcrRoot.path)
                                .relativize(URI.create(node.path))
                                .path
                        val jcrNode: Node
                        if (jcrRoot.hasNode(path)) {
                            jcrNode = jcrRoot.getNode(path)
                            this.logger.info {
                                    "Node %s already exists.".format(node.path)
                            }
                        } else {
                            jcrNode = jcrRoot.addNode(
                                    path, node.type)
                            this.logger.info {
                                "Node %s added.".format(jcrNode.path)
                            }
                        }
                        for (mixin in node.mixins) {
                            try {
                                jcrNode.addMixin(mixin)
                            } catch (e: RepositoryException) {
                                this.logger.warning {
                                    "Repo job unable to add mixin %s."
                                            .format(mixin)
                                }
                            }

                        }
                    } catch (e: RepositoryException) {
                        this.logger.severe {
                            "Repo job error adding node %s.".format(node.path)
                        }
                    }

                }
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
        this.lock.lock()
        try {
            this.isReady = true
            condition.signalAll()
        } finally {
            this.lock.unlock()
        }
    }

    private fun collectJobs(): List<RepositoryJob>? {
        this.jobs.clear()
        this.jobs.add(BuiltInJob)
        return this.jobs
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
