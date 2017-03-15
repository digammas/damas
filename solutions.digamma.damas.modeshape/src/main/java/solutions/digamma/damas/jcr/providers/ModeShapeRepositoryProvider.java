package solutions.digamma.damas.jcr.providers;

import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Configurations;
import solutions.digamma.damas.logging.Logbook;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;

/**
 * ModeShape JCR repository provider.
 *
 * @author Ahmad Shahwan
 */

public class ModeShapeRepositoryProvider {

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
    @Produces
    public Repository getRepository() {
        this.logger.info("Acquiring JCR repository.");
        Repository repository = null;
        for (RepositoryFactory factory :
                ServiceLoader.load(RepositoryFactory.class)) {
            this.logger.info("JCR repository factory found.");
            try {
                repository = factory.getRepository(this.getParameters());
            } catch (RepositoryException e) {
                this.logger.log(
                        Level.WARNING,
                        "Error acquiring JCR repository from factory.",
                        e);
            }
            if (repository != null) {
                this.logger.info("JCR repository implementation found.");
                return repository;
            }
        }
        this.logger.severe("No JCR implementation found.");
        throw new RuntimeException("No repository provider could be found.");
    }

    /**
     * Prepare repository parameters.
     *
     * @return
     */
    private Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>(this.parameters);
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
