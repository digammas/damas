package solutions.digamma.damas.rs.error;

import solutions.digamma.damas.common.*;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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
        context.register(
                new ExceptionReporter<NotFoundException>(
                    Status.NOT_FOUND){});
        context.register(
                new ExceptionReporter<AuthenticationException>(
                    Status.UNAUTHORIZED){});
        context.register(
                new ExceptionReporter<AuthorizationException>(
                    Status.FORBIDDEN){});
        context.register(
                new ExceptionReporter<ConflictException>(
                    Status.CONFLICT){});
        context.register(
                new ExceptionReporter<ResourceBusyException>(
                    Status.REQUEST_TIMEOUT){});
        context.register(
                new ExceptionReporter<InvalidArgumentException>(
                    Status.BAD_REQUEST){});
        context.register(
                new ExceptionReporter<UnsupportedActionException>(
                    Status.NOT_IMPLEMENTED){});
        context.register(new ExceptionReporter<WorkspaceException>(
                Status.INTERNAL_SERVER_ERROR){});
        context.register(new ExceptionReporter<>(
                Status.INTERNAL_SERVER_ERROR){});
        return true;
    }
}
