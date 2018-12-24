package solutions.digamma.damas.rs.error;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * A feature to report exception information when one is caught.
 *
 * @author Ahmad Shahwan
 */
@Provider
public class ExceptionReportFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(WorkspaceExceptionMapper.class);
        context.register(GenericExceptionMapper.class);
        return true;
    }
}
