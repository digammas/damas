package solutions.digamma.damas.jcr.repo;

import solutions.digamma.damas.logging.Logbook;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * JCR repository provider.
 *
 * @author Ahmad Shahwan
 */

public class RepositoryProvider {

    @Inject
    private Logbook logger;

    @Inject
    private RepositoryFactory factory;

    @Inject
    private RepositoryInitializer initializer;

    /**
     * Returns a JCR repository.
     *
     * @return Initialized repository.
     * @throws RepositoryException When a repository error occurs.
     */
    @Singleton @Produces
    public Repository getRepository() throws RepositoryException {
        this.logger.info("Acquiring JCR repository from repository factory.");
        Map<String, String> params = new HashMap<>();
        Repository repository = this.factory.getRepository(params);
        this.logger.info("Invoking initializer on acquired JCR repository.");
        this.initializer.initialize(repository);
        return repository;
    }
}
