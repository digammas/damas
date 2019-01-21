package solutions.digamma.damas.jcr.providers;

import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.logging.Logbook;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Map;
import java.util.ServiceLoader;

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

    @PostConstruct
    public void setUp() throws IOException {
        Files.createDirectories(Paths.get("repository/cdn/"));
        extractResource(
                "/repository/repository.json",
                "repository/repository.json");
        extractResource(
                "/repository/cdn/damas.cdn",
                "repository/cdn/damas.cdn");
    }

    @PreDestroy
    public void tearDown() {
        try {
            Files.walk(Paths.get("repository"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            System.err.println("IO Exception while cleaning up.");
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

    private void extractResource(String resourcePath, String distPath)
            throws IOException {
        try (InputStream stream = getClass()
                .getResourceAsStream(resourcePath)) {
            Files.copy(
                    stream,
                    Paths.get(distPath),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
