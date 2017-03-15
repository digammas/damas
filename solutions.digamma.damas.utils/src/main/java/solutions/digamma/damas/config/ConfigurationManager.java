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
     * Return all configurations that start with the provider postfix.
     *
     * @param postfix
     * @return
     */
    Map<String, Object> getConfigurations(String postfix);
}
