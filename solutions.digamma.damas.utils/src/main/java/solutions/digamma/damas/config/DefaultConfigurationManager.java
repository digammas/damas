package solutions.digamma.damas.config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Default configuration manager implementation.
 *
 * @author Ahmad Shahwan
 */
public class DefaultConfigurationManager implements ConfigurationManager {

    /**
     * Home directory system property.
     */
    public static final String HOME_DIR = "home.dir";

    /**
     * Default home directory.
     */
    private static final String DEFAULT_HOME_DIR = ".";

    private static final String CONF_FILE_NAME = "conf.properties";

    @Inject
    private Logger logger;

    private Properties properties;

    /**
     * No-args constructor.
     */
    public DefaultConfigurationManager() {
    }

    /**
     * Initialize manager.
     */
    @PostConstruct
    public void init() {
        this.logger.info("Initializing configuration service.");
        File homeDir = getHomeDir();
        File confFile = new File(homeDir, CONF_FILE_NAME);
        this.properties = new Properties();
        if (confFile.isFile()) {
            this.logger.info(() -> String.format(
                    "Configuration file found at %s.", confFile.getPath()));
            try {
                this.properties.load(new FileReader(confFile));
                this.logger.info(() -> String.format(
                        "%d configs loaded from file", this.properties.size()));
            } catch (IOException e) {
                this.logger.log(Level.SEVERE, "Error reading conf file.", e);
            }
        } else {
            this.logger.info(() -> String.format(
                    "No configuration file found at %s.", confFile.getPath()));
        }
        this.properties.putAll(System.getenv());
        this.properties.putAll(System.getProperties());
        this.properties.put(Configurations.HOME, homeDir.getAbsolutePath());
        this.logger.info("System properties added to configurations.");
    }

    @Override
    public String getString(String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public Integer getInteger(String key) {
        if (!this.properties.contains(key)) {
            return null;
        }
        try {
            return Integer.parseInt(this.properties.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Map<String, Object> getConfigurations(String postfix) {
        return this.properties
                .entrySet()
                .stream()
                .filter(e -> e.getKey().toString().startsWith(postfix))
                .collect(Collectors.toMap(
                        p -> p.getKey().toString(),
                        Map.Entry::getValue));
    }

    /**
     * Obtain home directory.
     *
     * @return home directory file
     */
    private File getHomeDir() {
        String homePath = System.getProperty(HOME_DIR);
        if (homePath != null) {
            File homeDir = new File(homePath);
            if (homeDir.isDirectory()) {
                this.logger.info(() -> String.format(
                        "Home directory at %s.", homeDir.getPath()));
                return homeDir;
            }
        }
        this.logger.info("Home directory defaults to current directory.");
        return new File(DEFAULT_HOME_DIR);
    }
}
