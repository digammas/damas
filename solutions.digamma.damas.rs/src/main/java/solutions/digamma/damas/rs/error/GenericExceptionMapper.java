package solutions.digamma.damas.rs.error;

import solutions.digamma.damas.common.AuthenticationException;
import solutions.digamma.damas.common.AuthorizationException;
import solutions.digamma.damas.common.ConflictException;
import solutions.digamma.damas.common.InvalidArgumentException;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.common.ResourceBusyException;
import solutions.digamma.damas.common.UnsupportedOperationException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Checked exception mapper.
 *
 * @author Ahmad Shahwan
 */
@Provider
public class GenericExceptionMapper
        implements ExceptionMapper<Throwable> {

    @Inject
    private Logger logger;

    @Override
    public Response toResponse(Throwable e) {
        log(e);
        return Response
            .status(toStatusCode(e))
            .entity(new ExceptionReport(e))
            .build();
    }

    private void log(WorkspaceException e) {
        if (e.getLogLevel() == Level.SEVERE) {
            this.logger.log(Level.SEVERE, "Checked exception.", e);
        } else {
            this.logger.log(e.getLogLevel(), e.getMessage());
        }
    }

    private void log(Throwable e) {
        if (e instanceof WorkspaceException) {
            log((WorkspaceException) e);
        } else {
            this.logger.log(Level.SEVERE, "Unchecked exception.", e);
        }
    }

    private static int toStatusCode(Throwable e) {
        if (e instanceof NotFoundException) {
            /* Not Found */
            return 404;
        }
        if (e instanceof AuthenticationException) {
            /* Unauthorized */
            return 401;
        }
        if (e instanceof AuthorizationException) {
            /* Forbidden */
            return 403;
        }
        if (e instanceof ConflictException) {
            /* Conflict */
            return 409;
        }
        if (e instanceof ResourceBusyException) {
            /* Too Many Requests */
            return 429;
        }
        if (e instanceof InvalidArgumentException) {
            /* Unprocessable Entity */
            return 422;
        }
        if (e instanceof UnsupportedOperationException) {
            /* Not Implemented */
            return 501;
        }
        /* Internal Server Error */
        return 500;
    }
}
