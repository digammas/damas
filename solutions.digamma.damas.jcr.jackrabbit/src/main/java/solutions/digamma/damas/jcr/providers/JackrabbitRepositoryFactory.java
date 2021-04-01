package solutions.digamma.damas.jcr.providers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import org.apache.jackrabbit.core.RepositoryContext;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;
import solutions.digamma.damas.logging.Logbook;

/**
 * Jackrabbit JCR repository provider.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JackrabbitRepositoryFactory implements RepositoryFactory {

    private static final String CONF_RESOURCE = "/repository/repository.xml";

    @Inject
    private Logbook logger;

    @Inject
    @Configuration("repository.home")
    @Fallback("storage")
    private String repositoryHome;

    /**
     * Repository context instance.
     */
    private RepositoryContext context;

    @PostConstruct
    public void setUp() {
        try {
            File home = this.prepareHome();
            RepositoryConfig conf = RepositoryConfig.create(home);
            this.context = RepositoryContext.create(conf);
        } catch (ConfigurationException e) {
            throw new IllegalStateException(
                    "Unable to configure Jackrabbit repository.", e);
        } catch (RepositoryException e) {
            throw new IllegalStateException(
                    "Unable to create Jackrabbit repository context.", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (this.context != null) {
            RepositoryImpl repo = this.context.getRepository();
            if (repo != null) {
                repo.shutdown();
            }
        }
    }

    private File prepareHome() {
        File home = new File(this.repositoryHome);
        this.logger.info("Repository home at %s", home.getPath());
        if (!home.isDirectory()) {
            this.logger.info("Creating repository home directory");
            home.mkdir();
        }
        File configFile = new File(this.repositoryHome, "repository.xml");
        this.logger.info("Extracting config to %s", configFile.getPath());
        try (InputStream stream = this.getClass()
                .getResourceAsStream(CONF_RESOURCE)) {
            Files.copy(
                    stream,
                    Paths.get(configFile.getPath()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to initiate repository home.", e);
        }
        return home;
    }

    /**
     * Returns a JCR repository.
     *
     * @return Jackrabbit JCR repository
     */
    @Override
    public Repository getRepository(Map params) {
        return this.context.getRepository();
    }
}
