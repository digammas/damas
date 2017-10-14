package solutions.digamma.damas.rs.error;

import solutions.digamma.damas.AuthenticationException;
import solutions.digamma.damas.AuthorizationException;
import solutions.digamma.damas.ConflictException;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.ResourceBusyException;
import solutions.digamma.damas.UnsupportedOperationException;

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

    private void log(DocumentException e) {
        if (e.getLogLevel() == Level.SEVERE) {
            this.logger.log(Level.SEVERE, "Checked exception.", e);
        } else {
            this.logger.log(e.getLogLevel(), e.getMessage());
        }
    }

    private void log(Throwable e) {
        if (e instanceof DocumentException) {
            log((DocumentException) e);
        } else {
            this.logger.log(Level.SEVERE, "Unchecked exception.", e);
        }
    }

    private static int toStatusCode(Throwable e) {
        if (e instanceof NotFoundException) {
            return 404;
        }
        if (e instanceof AuthenticationException) {
            return 401;
        }
        if (e instanceof AuthorizationException) {
            return 403;
        }
        if (e instanceof ConflictException) {
            return 409;
        }
        if (e instanceof ResourceBusyException) {
            return 429;
        }
        if (e instanceof UnsupportedOperationException) {
            return 501;
        }
        return 500;
    }
}
