package solutions.digamma.damas.logging;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;

/**
 * Logger factory.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class LoggerFactory {

    /**
     * Get logger for the underling injection point.
     *
     * @param ip
     * @return
     */
    @Produces
    public Logbook getLogger(InjectionPoint ip) {
        return new Logbook(ip.getMember().getDeclaringClass().getName());
    }
}
