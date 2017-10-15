package solutions.digamma.damas.jcr;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.rules.ExternalResource;

/**
 * CDI container test rule.
 *
 * @author Ahmad Shahwan
 */
public class WeldRule extends ExternalResource {

    private WeldContainer container;

    /**
     * Constructor.
     */
    public WeldRule() {
    }

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
    public <T> T inject(Class<T> klass) {
        return container.select(klass).get();
    }
}