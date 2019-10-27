package solutions.digamma.damas.logging;

import java.util.logging.Handler;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Logger factory.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class LoggerFactory {

    @Inject
    private Instance<Handler> handlers;

    /**
     * Get logger for the underling injection point.
     *
     * @param ip    Injection point.
     * @return      Logbook instance.
     */
    @Produces
    public Logbook getLogger(InjectionPoint ip) {
        String name = ip.getMember().getDeclaringClass().getName();
        return new Logbook(name, this.handlers);
    }
}
