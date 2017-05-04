package solutions.digamma.damas.jcr.repo;

import javax.jcr.Repository;

/**
 * Repository initializer.
 *
 * @author Ahmad Shahwan
 */
public interface RepositoryInitializer {

    /**
     * Initialize repository. This means running all initialization jobs against
     * the given repository.
     *
     * @param repository The repository to initialize.
     */
    void initialize(Repository repository);

    /**
     * Wait until initialization is done. If initialization is already done,
     * this method returns immediately. Otherwise, it block until initialization
     * is done.
     *
     * @throws InterruptedException
     */
    void waitUntilDone() throws InterruptedException;

    /**
     * Wait until initialization is done, or timeout reached. If initialization
     * is already done, this method returns {@code true} immediately. Otherwise,
     * it block until either initialization is done, in which case it returns
     * {@code true}, or timeout reached in which case it return {@code false}.
     *
     * @param timeout Timeout in seconds.
     * @return {@code true} if initialization is done before {@code timeout} is
     * reached, {@code false} otherwise.
     * @throws InterruptedException
     */
    boolean waitUntilDone(long timeout) throws InterruptedException;
}
