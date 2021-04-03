package solutions.digamma.damas.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.rules.ExternalResource;

/**
 * CDI container test rule.
 *
 * @author Ahmad Shahwan
 */
public class ContainerRule extends ExternalResource {

    private WeldContainer container;

    @Override
    protected void before() {
        /* Initialization is done here so that container can be used by runner.
         */
        this.container = new Weld().initialize();
    }

    @Override
    protected void after() {
        if (this.container != null) {
            this.container.close();
        }
    }

    /**
     * Find dependency convert type {@code klass}.
     *
     * @param klass
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> klass) {
        return container.select(klass).get();
    }
}
