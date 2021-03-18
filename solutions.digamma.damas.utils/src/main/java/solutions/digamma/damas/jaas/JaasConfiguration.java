package solutions.digamma.damas.jaas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * JAAS configuration wrapper that allows adding new reams, or policies, without
 * affecting existing ones.
 *
 * An instance of this class installs itself as the default, runtime-wide, JAAS
 * configuration, after keeping a reference to the previously installed one.
 *
 * For each demand, that boils down to a call to {@code
 * getAppConfigurationEntry()}, a bean of this type checks on all{@link
 * Configuration} bean implementations qualified with {@link Realm}.
 * Configuration is then delegated to {@link Realm}-qualified beans whose
 * qualification values contains the policy name, adding up configurations.
 * If no such bean is found the demand is delegated to the previously installed
 * configuration.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JaasConfiguration extends Configuration {

    public static final String REALM = "damas";

    private final Configuration delegate;
    private Map<String, List<Configuration>> configs;

    /**
     * Public constructor.
     *
     * This constructor install the current JAAS configuration into the runtime.
     * This must happen at construction, and not at CDI post-construction, to
     * allow JAAS-aware bean to create login contexts with expected realms.
     */
    public JaasConfiguration() {
        this.delegate = Configuration.getConfiguration();
        Configuration.setConfiguration(this);
    }

    /**
     * Allow eager initialization of bean.
     *
     * @param event         application initialization event
     */
    public void onAppInitialization(
            @Observes @Initialized(ApplicationScoped.class) Object event) {
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        List<Configuration> registered = this.configs.get(name);
        if (registered == null || registered.isEmpty()) {
            return delegate.getAppConfigurationEntry(name);
        }
        return registered.stream()
                .map(c -> c.getAppConfigurationEntry(name))
                .flatMap(Arrays::stream)
                .toArray(AppConfigurationEntry[]::new);
    }

    /**
     * Bind to all beans of type {@link Configuration} qualified with
     * {@link Realm}.
     *
     * @param instances     instances of configuration beans
     */
    @Inject
    public void setInstances(@Realm("") Instance<Configuration> instances) {
        this.configs = new HashMap<>();
        for (Configuration config : instances) {
            Realm realm = config.getClass().getAnnotation(Realm.class);
            if (realm != null) {
                for (String value : realm.value()) {
                    List<Configuration> list = this.configs.get(value);
                    if (list == null) {
                        list = new ArrayList<>(1);
                    }
                    list.add(config);
                    this.configs.put(value, list);
                }
            }
        }
    }
}
