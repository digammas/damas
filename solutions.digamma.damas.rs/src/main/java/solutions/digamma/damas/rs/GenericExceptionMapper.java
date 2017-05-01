package solutions.digamma.damas.rs;

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
    private Logger log;

    @Override
    public Response toResponse(Throwable e) {
        if (isSever(e)) {
            this.log.log(Level.SEVERE, "Unchecked exception.", e);
        }
        return Response
            .status(toStatusCode(e))
            .entity(new ExceptionReport(e))
            .build();
    }

    private static boolean isSever(Throwable e) {
        return !(e instanceof DocumentException) ||
            ((DocumentException) e)
                    .getSeverity() == DocumentException.Severity.HIGH;
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

    /**
     * Exception report.
     */
    public static class ExceptionReport {

        private Throwable exception;

        private ExceptionReport(Throwable e) {
            this.exception = e;
        }

        /**
         * Error message.
         *
         * @return
         */
        public String getMessage() {
            return this.exception.getMessage();
        }

        /**
         * Error cause.
         *
         * @return
         */
        public ExceptionReport getCause() {
            Throwable cause = this.exception.getCause();
            return cause == null ? null : new ExceptionReport(cause);
        }
    }
}
