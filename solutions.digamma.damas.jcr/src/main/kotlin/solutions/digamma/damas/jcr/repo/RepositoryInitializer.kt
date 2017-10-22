package solutions.digamma.damas.jcr.repo

import javax.jcr.Repository

/**
 * Repository initializer.
 *
 * @author Ahmad Shahwan
 */
interface RepositoryInitializer {

    /**
     * Initialize repository. This means running all initialization jobs against
     * the given repository.
     *
     * @param repository The repository to initialize.
     */
    fun initialize(repository: Repository)

    /**
     * Wait until initialization is done. If initialization is already done,
     * this method returns immediately. Otherwise, it block until initialization
     * is done.
     *
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    fun waitUntilDone()

    /**
     * Wait until initialization is done, or timeout reached. If initialization
     * is already done, this method returns `true` immediately. Otherwise,
     * it block until either initialization is done, in which case it returns
     * `true`, or timeout reached in which case it return `false`.
     *
     * @param timeout Timeout in seconds.
     * @return `true` if initialization is done before `timeout` is
     * reached, `false` otherwise.
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    fun waitUntilDone(timeout: Long): Boolean
}
