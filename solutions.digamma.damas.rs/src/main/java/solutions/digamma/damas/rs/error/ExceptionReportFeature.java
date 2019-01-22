package solutions.digamma.damas.rs.error;

import solutions.digamma.damas.common.*;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

/**
 * A feature to report exception information when one is caught.
 *
 * @author Ahmad Shahwan
 */
@Provider
public class ExceptionReportFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new WorkspaceExceptionMapper<NotFoundException>(Response.Status.NOT_FOUND){});
        context.register(new WorkspaceExceptionMapper<AuthenticationException>(Response.Status.UNAUTHORIZED){});
        context.register(new WorkspaceExceptionMapper<AuthorizationException>(Response.Status.FORBIDDEN){});
        context.register(new WorkspaceExceptionMapper<ConflictException>(Response.Status.CONFLICT){});
        context.register(new WorkspaceExceptionMapper<ResourceBusyException>(Response.Status.REQUEST_TIMEOUT){});
        context.register(new WorkspaceExceptionMapper<InvalidArgumentException>(Response.Status.BAD_REQUEST){});
        context.register(new WorkspaceExceptionMapper<UnsupportedActionException>(Response.Status.NOT_IMPLEMENTED){});
        context.register(new WorkspaceExceptionMapper<>(Response.Status.INTERNAL_SERVER_ERROR){});
        context.register(new GenericExceptionMapper<>(){});
        return true;
    }
}
