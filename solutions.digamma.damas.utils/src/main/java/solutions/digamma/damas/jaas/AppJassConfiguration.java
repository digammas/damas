package solutions.digamma.damas.jaas;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ahmad Shahwan
 */
@Singleton
public class AppJassConfiguration extends Configuration {

    private Configuration delegate;
    private Map<String, Configuration> configs;

    /**
     * Public constructor.
     *
     * This constructor install the current JAAS configuration into the runtime.
     * This must happen at construction, and not at CDI post-construction, to
     * allow JAAS-aware bean to create login contexts with expected realms.
     */
    public AppJassConfiguration() {
        this.delegate = Configuration.getConfiguration();
        Configuration.setConfiguration(this);
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        Configuration config = this.configs.get(name);
        return (config != null ? config : delegate)
                .getAppConfigurationEntry(name);
    }

    @Inject
    public void setInstances(
            @Realm("") Instance<Configuration> instances) {
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
