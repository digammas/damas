package solutions.digamma.damas.jcr.providers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import solutions.digamma.damas.auth.JaasConfiguration;
import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;
import solutions.digamma.damas.logging.Logbook;

/**
 * ModeShape JCR repository provider.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class ModeShapeRepositoryFactory implements RepositoryFactory {

    private static final String JCR_URL = "org.modeshape.jcr.URL";

    private static final String JSON_RESOURCE = "/repository/repository.json";

    @Inject
    @Configuration("repository.home")
    @Fallback("storage")
    private String repositoryHome;

    @Inject
    private Logbook logger;

    /**
     * Concurrency dependency. This is to make sure that JAAS is correctly
     * configured before starting the repository.
     */
    @Inject
    private JaasConfiguration jaasConfiguration;

    private String jsonFile;

    @PostConstruct
    public void setUp() {
        try {
            Files.createDirectories(Paths.get(this.repositoryHome));
            this.logger.info("Repository home at %s", this.repositoryHome);
            this.jsonFile = Paths.get(this.repositoryHome, "repository.json")
                    .toString();
            try (InputStream stream = getClass()
                    .getResourceAsStream(JSON_RESOURCE)) {
                Files.copy(
                        stream,
                        Paths.get(jsonFile),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            System.setProperty("repository.home", this.repositoryHome);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to create CND file for repository", e);
        }
    }

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
    private Map<Object, Object> getParameters(Map<?, ?> params) {
        /* Add the one mandatory parameter */
        Map<Object, Object> paramsWithUrl = new HashMap<>(params);
        this.logger.info("JCR repository setting is at %s.", this.jsonFile);
        paramsWithUrl.put(JCR_URL, this.jsonFile);
        return paramsWithUrl;
    }
}
