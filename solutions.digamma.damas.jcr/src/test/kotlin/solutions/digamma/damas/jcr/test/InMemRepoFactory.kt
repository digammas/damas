package solutions.digamma.damas.jcr.test

import org.modeshape.common.collection.SimpleProblems
import org.modeshape.jcr.CndImporter
import org.modeshape.jcr.ExecutionContext
import solutions.digamma.damas.logging.Logbook

import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.Repository
import javax.jcr.RepositoryException
import javax.jcr.RepositoryFactory
import javax.jcr.Session
import javax.jcr.nodetype.NodeTypeDefinition
import javax.jcr.nodetype.NodeTypeManager
import java.io.IOException
import java.io.InputStream
import java.util.ServiceLoader

/**
 * In-memory repository factory.
 *
 * @author Ahmad Shahwan
 */
@Singleton
class InMemRepoFactory : RepositoryFactory {

    @Inject
    private lateinit var logger: Logbook

    @Throws(RepositoryException::class)
    override fun getRepository(params: MutableMap<*, *>): Repository {
        this.logger.info("Acquiring JCR repository from Java service loader.")
        var repository: Repository?
        for (factory in ServiceLoader.load(RepositoryFactory::class.java)) {
            this.logger.info("JCR repository factory service found.")
            repository = factory.getRepository(
                    this.getParameters(params as MutableMap<String, Any>))
            if (repository == null) {
                continue
            }
            this.logger.info("JCR repository implementation found.")
            val cnd = CndImporter(ExecutionContext.DEFAULT_CONTEXT)
            val problems = SimpleProblems()
            try {
                val url = this.javaClass
                        .getResourceAsStream("repository/cdn/damas.cdn")
                cnd.importFrom(url, problems, null)
            } catch (e: IOException) {
                throw RepositoryException("CND file not found.", e)
            }

            val types = cnd.nodeTypeDefinitions
            val session = repository.login()
            val manager = session.workspace
                    .nodeTypeManager
            manager.registerNodeTypes(
                    types.toTypedArray(), true)
            session.logout()
            return repository
        }
        this.logger.severe("No JCR implementation found.")
        throw RepositoryException("No repository factory service found.")
    }

    /**
     * Prepare repository parameters.
     *
     * @return
     */
    private fun getParameters(params: MutableMap<String, Any>):
            Map<String, Any> {
        val url = this.javaClass.getResource("/repository/in_mem_repo.json")
                .toString()
        this.logger.info("JCR repository home URL is set to %s.", url)
        params.put(JCR_URL, url)
        return params
    }

    companion object {

        private val JCR_URL = "org.modeshape.jcr.URL"
    }
}
