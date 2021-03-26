package solutions.digamma.damas.config;

import java.util.Map;

/**
 * Configuration manager.
 *
 * @author Ahmad Shahwan
 */
public interface ConfigurationManager {

    /**
     * Get string configuration.
     *
     * @param key Configuration key.
     * @return Configuration value, or {@code null} if non is found for the
     * provided key.
     */
    String getString(String key);

    /**
     * Get integer configuration.
     *
     * @param key Configuration key.
     * @return Configuration value, or {@code null} if non is found for the
     * provided key.
     */
    Integer getInteger(String key);

    /**
     * Get integer configuration.
     *
     * @param key Configuration key.
     * @return Configuration value, or {@code null} if non is found for the
     * provided key.
     */
    Boolean getBoolean(String key);

    /**
     * Return all configurations that start with the provider postfix.
     *
     * @param postfix   configuration prefix
     * @return          a map of all configurations starting with prefix
     */
    Map<String, Object> getConfigurations(String postfix);
}
