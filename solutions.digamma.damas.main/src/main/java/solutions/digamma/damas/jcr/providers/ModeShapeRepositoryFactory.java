package solutions.digamma.damas.jcr.providers;

import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.logging.Logbook;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import java.io.File;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;

/**
 * ModeShape JCR repository provider.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class ModeShapeRepositoryFactory implements RepositoryFactory {

    private static final String POSTFIX = "org.modeshape.";
    private static final String JCR_URL = "org.modeshape.jcr.URL";

    @Inject @Configuration(POSTFIX)
    private Map<String, Object> parameters;

    @Inject
    private Logbook logger;

    /**
     * Returns a JCR repository.
     *
     * @return
     */
    @Override
    public Repository getRepository(Map params) throws RepositoryException {
        this.logger.info("Acquiring JCR repository from service loader.");
        for (RepositoryFactory factory :
                ServiceLoader.load(RepositoryFactory.class)) {
            this.logger.info("JCR repository factory service found.");
            Repository repository = factory
                    .getRepository(this.getParameters(params));
            if (repository != null) {
                this.logger.info("JCR repository implementation retrieved.");
                return repository;
            }
        }
        this.logger.severe("No JCR implementation found.");
        throw new RepositoryException("No repository factory service found.");
    }

    /**
     * Prepare repository parameters.
     *
     * @return
     */
    private Map<String, Object> getParameters(Map params) {
        /* One mandatory parameter */
        if (!params.containsKey(JCR_URL)) {
            String url = new File(
                    "repository", "repository.json").toURI().toString();
            this.logger.info("JCR repository home URL is set to %s.", url);
            params.put(JCR_URL, url);
        }
        return params;
    }
}
