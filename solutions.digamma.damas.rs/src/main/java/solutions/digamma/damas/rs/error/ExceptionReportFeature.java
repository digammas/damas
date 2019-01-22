package solutions.digamma.damas.rs.error;

import solutions.digamma.damas.common.AuthenticationException;
import solutions.digamma.damas.common.AuthorizationException;
import solutions.digamma.damas.common.ConflictException;
import solutions.digamma.damas.common.InvalidArgumentException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.common.ResourceBusyException;
import solutions.digamma.damas.common.UnsupportedActionException;
import solutions.digamma.damas.common.WorkspaceException;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
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
        /* Subclasses must be created before instantiation.
         * This is to preserve generic information. Otherwise erasure would
         * prevent JAX-RS container from accessing exception types.
         */
        context
            .register(
                new ExceptionReporter<NotFoundException>(
                    Status.NOT_FOUND){})
            .register(
                new ExceptionReporter<AuthenticationException>(
                    Status.UNAUTHORIZED){})
            .register(
                new ExceptionReporter<AuthorizationException>(
                    Status.FORBIDDEN){})
            .register(
                new ExceptionReporter<ConflictException>(
                    Status.CONFLICT){})
            .register(
                new ExceptionReporter<ResourceBusyException>(
                    Status.REQUEST_TIMEOUT){})
            .register(
                new ExceptionReporter<InvalidArgumentException>(
                    Status.BAD_REQUEST){})
            .register(
                new ExceptionReporter<UnsupportedActionException>(
                    Status.NOT_IMPLEMENTED){})
            .register(
                new ExceptionReporter<WorkspaceException>(
                    Status.INTERNAL_SERVER_ERROR){})
            .register(
                new ExceptionReporter<>(
                    Status.INTERNAL_SERVER_ERROR){});
        return true;
    }
}
