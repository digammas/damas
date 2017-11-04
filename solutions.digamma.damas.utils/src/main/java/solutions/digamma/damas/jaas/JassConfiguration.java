package solutions.digamma.damas.jaas;

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
 * <br/>
 * An instance of this class installs itself as the default, runtime-wide, JAAS
 * configuration, after keeping a reference to the previously installed one.
 * <br/>
 * For each demand, that boils down to a call to {@code
 * getAppConfigurationEntry()}, a bean of this type checks on all {@link
 * Configuration} bean implementations qualified with {@link Realm}. If values
 * of the {@link Realm} qualification contains the policy name, configuration is
 * then delegated to that {@link Realm}-qualified bean. If no such bean is found
 * the demand is delegated to the previously installed configuration.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JassConfiguration extends Configuration {

    private Configuration delegate;
    private Map<String, Configuration> configs;

    /**
     * Public constructor.
     *
     * This constructor install the current JAAS configuration into the runtime.
     * This must happen at construction, and not at CDI post-construction, to
     * allow JAAS-aware bean to create login contexts with expected realms.
     */
    public JassConfiguration() {
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
        Configuration config = this.configs.get(name);
        return (config != null ? config : delegate)
                .getAppConfigurationEntry(name);
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
                    this.configs.put(value, config);
                }
            }
        }
    }
}
