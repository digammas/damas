package solutions.digamma.damas.config;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.util.Map;

/**
 * Configuration provider.
 *
 * @author Ahmad Shahwan
 */
public class ConfigurationProvider {

    public ConfigurationProvider() {
    }

    @Inject
    private ConfigurationManager manager;

    @Produces
    @Configuration("")
    public String getString(InjectionPoint ip) {
        String key = getKey(ip);
        String value = this.manager.getString(key);
        if (value == null) {
            throw new RuntimeException(String.format(
                    "Missing required configuration %s.", key));
        }
        return value;
    }

    @Produces
    @Configuration("")
    @Optional
    public String getOptionalString(InjectionPoint ip) {
        return this.manager.getString(getKey(ip));
    }

    @Produces
    @Configuration("")
    @Fallback("")
    public String getStringOrFallback(InjectionPoint ip) {
        String value = this.manager.getString(getKey(ip));
        if (value != null) {
            return value;
        }
        return getFallback(ip);
    }

    @Produces
    @Configuration("")
    public Integer getInteger(InjectionPoint ip) {
        String key = getKey(ip);
        Integer value = this.manager.getInteger(key);
        if (value == null) {
            throw new RuntimeException(String.format(
                    "Missing required configuration %s.", key));
        }
        return value;
    }

    @Produces
    @Configuration("")
    @Optional
    public Integer getOptionalInteger(InjectionPoint ip) {
        return this.manager.getInteger(getKey(ip));
    }

    @Produces
    @Configuration("")
    @Fallback("")
    public Integer getIntegerOrFallback(InjectionPoint ip) {
        String key = getKey(ip);
        Integer value = this.manager.getInteger(key);
        String def = null;
        if (value != null) {
            return value;
        }
        try {
            def = getFallback(ip);
            return Integer.parseInt(def);
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format(
                    "Illegal default %s for configuration %s.", def, key), e);
        }
    }

    @Produces
    @Configuration("")
    public Map<String, Object> getConfigurations(InjectionPoint ip) {
        String postfix = getKey(ip);
        return this.manager.getConfigurations(postfix);
    }

    private static String getFallback(InjectionPoint ip) {
        return ip
                .getAnnotated()
                .getAnnotation(Fallback.class)
                .value();
    }

    private static String getKey(InjectionPoint ip) {
        return ip
                .getAnnotated()
                .getAnnotation(Configuration.class)
                .value();
    }
}
