package solutions.digamma.damas.config;

import java.util.Map;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

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
        String value = this.manager.getString(getKey(ip));
        return value != null ? value : getFallback(ip);
    }

    @Produces
    @Configuration("")
    public Integer getInteger(InjectionPoint ip) {
        String key = getKey(ip);
        Integer value = this.manager.getInteger(key);
        if (value != null) {
            return value;
        }
        String fallback = getFallback(ip);
        try {
            return Integer.parseInt(fallback);
        } catch (NumberFormatException e) {
            String message =
                "Illegal default %s for configuration %s, integer expected.";
            message = String.format(message, fallback, key);
            throw new UnsatisfiedResolutionException(message, e);
        }
    }

    @Produces
    @Configuration("")
    public Map<String, Object> getConfigurations(InjectionPoint ip) {
        String key = getKey(ip);
        return this.manager.getConfigurations(key);
    }

    private static String getFallback(InjectionPoint ip) {
        Fallback fallback = ip.getAnnotated()
                .getAnnotation(Fallback.class);
        if (fallback == null && !isOptional(ip)) {
            throw new UnsatisfiedResolutionException(
                    String.format("Unsatisfied configuration %s.", getKey(ip)));
        }
        return fallback != null ? fallback.value() : null;
    }

    private static String getKey(InjectionPoint ip) {
        return ip
                .getAnnotated()
                .getAnnotation(Configuration.class)
                .value();
    }

    private static boolean isOptional(InjectionPoint ip) {
        return ip
                .getAnnotated()
                .getAnnotation(Configuration.class)
                .optional();
    }
}
