package solutions.digamma.damas.jcr.repo

import solutions.digamma.damas.logging.Logbook

import javax.enterprise.inject.Produces
import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.Repository
import javax.jcr.RepositoryException
import javax.jcr.RepositoryFactory
import java.util.HashMap

/**
 * JCR repository provider.
 *
 * @author Ahmad Shahwan
 */

class RepositoryProvider {

    @Inject
    private var logger: Logbook? = null

    @Inject
    private var factory: RepositoryFactory? = null

    @Inject
    private var initializer: RepositoryInitializer? = null

    /**
     * Returns a JCR repository.
     *
     * @return Initialized repository.
     * @throws RepositoryException When a repository error occurs.
     */
    val repository: Repository
        @Singleton
        @Produces
        @Throws(RepositoryException::class)
        get() {
            this.logger!!.info("Acquiring JCR repository from repository factory.")
            val params = HashMap<String, String>()
            val repository = this.factory!!.getRepository(params)
            this.logger?.info("Invoking initializer on acquired JCR repository.")
            this.initializer!!.initialize(repository)
            return repository
        }
}
