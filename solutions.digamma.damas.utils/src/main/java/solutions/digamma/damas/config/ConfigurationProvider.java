package solutions.digamma.damas.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
        return this.getString(getKeys(ip)).orElseGet(() -> getFallback(ip));
    }

    public Optional<String> getString(String[] keys) {
        return Arrays.stream(keys)
                .map(this.manager::getString)
                .filter(Objects::nonNull)
                .findFirst();
    }

    @Produces
    @Configuration("")
    public Integer getInteger(InjectionPoint ip) {
        return Arrays.stream(getKeys(ip))
                .map(this.manager::getInteger)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> this.getIntegerFallback(ip));
    }

    private Integer getIntegerFallback(InjectionPoint ip) {
        String fallback = getFallback(ip);
        try {
            return Integer.parseInt(fallback);
        } catch (NumberFormatException e) {
            String message =
                    "Illegal default %s for configuration, integer expected.";
            message = String.format(message, fallback);
            throw new UnsatisfiedResolutionException(message, e);
        }
    }

    @Produces
    @Configuration("")
    public Map<String, Object> getConfigurations(InjectionPoint ip) {
        return Arrays.stream(getKeys(ip))
            .map(this.manager::getConfigurations)
            .map(Map::entrySet)
            .flatMap(Collection::stream)
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private static String getFallback(InjectionPoint ip) {
        Fallback fallback = ip.getAnnotated()
                .getAnnotation(Fallback.class);
        if (fallback == null && !isOptional(ip)) {
            throw new UnsatisfiedResolutionException(
                    String.format("Unsatisfied configuration %s.", getKeys(ip)));
        }
        return fallback != null ? fallback.value() : null;
    }

    private static String[] getKeys(InjectionPoint ip) {
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
