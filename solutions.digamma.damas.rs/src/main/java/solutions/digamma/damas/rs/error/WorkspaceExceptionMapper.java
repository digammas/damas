package solutions.digamma.damas.rs.error;

import solutions.digamma.damas.common.AuthenticationException;
import solutions.digamma.damas.common.AuthorizationException;
import solutions.digamma.damas.common.ConflictException;
import solutions.digamma.damas.common.InvalidArgumentException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.common.ResourceBusyException;
import solutions.digamma.damas.common.UnsupportedActionException;
import solutions.digamma.damas.common.WorkspaceException;

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
public class WorkspaceExceptionMapper
        implements ExceptionMapper<WorkspaceException> {

    @Inject
    private Logger logger;

    @Override
    public Response toResponse(WorkspaceException e) {
        log(e);
        return Response
            .status(toStatus(e))
            .entity(new ExceptionReport(e))
            .build();
    }

    private void log(WorkspaceException e) {
        switch (e.getOrigin()) {
            case INTERNAL:
                this.logger.log(Level.SEVERE, "Checked exception.", e);
                break;
            case API:
                this.logger.log(Level.WARNING, e.getMessage());
                break;
            case DOMAIN:
                this.logger.log(Level.INFO, e.getMessage());
                break;
        }
    }

    private static Response.Status toStatus(Throwable e) {
        if (e instanceof NotFoundException) {
            /* Not Found */
            return Response.Status.NOT_FOUND;
        }
        if (e instanceof AuthenticationException) {
            /* Unauthorized */
            return Response.Status.UNAUTHORIZED;
        }
        if (e instanceof AuthorizationException) {
            /* Forbidden */
            return Response.Status.FORBIDDEN;
        }
        if (e instanceof ConflictException) {
            /* Conflict */
            return Response.Status.CONFLICT;
        }
        if (e instanceof ResourceBusyException) {
            /* Request timeout */
            return Response.Status.REQUEST_TIMEOUT;
        }
        if (e instanceof InvalidArgumentException) {
            /* Bad request */
            return Response.Status.BAD_REQUEST;
        }
        if (e instanceof UnsupportedActionException) {
            /* Not Implemented */
            return Response.Status.NOT_IMPLEMENTED;
        }
        /* Internal Server Error */
        return Response.Status.INTERNAL_SERVER_ERROR;
    }
}
