package solutions.digamma.damas.rs.log;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * A feature that log incoming requests and outgoing responses.
 *
 * @author Ahmad Shahwan
 */
public class LogFeature implements Feature {

    @Override
    public boolean configure(FeatureContext featureContext) {
        featureContext.register(LogRequestFilter.class);
        featureContext.register(LogResponseFilter.class);
        return true;
    }
}
