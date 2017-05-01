package solutions.digamma.damas.rs;

import solutions.digamma.damas.*;
import solutions.digamma.damas.UnsupportedOperationException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.annotation.XmlTransient;
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
        this.log.log(Level.SEVERE, "Unchecked exception.", e);
        return Response
            .status(toStatusCode(e))
            .entity(new MutedException(e))
            .build();
    }

    public static int toStatusCode(Throwable e) {
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
        if (e instanceof ConflictException) {
            return 429;
        }
        if (e instanceof UnsupportedOperationException) {
            return 501;
        }
        return 500;
    }

    /**
     * Exception wrapper that mutes the stack trace.
     */
    public static class MutedException extends Exception {

        private Throwable shadow;

        private MutedException(Throwable e) {
            this.shadow = e;
        }

        @XmlTransient
        @Override
        public StackTraceElement[] getStackTrace() {
            return this.shadow.getStackTrace();
        }

        @Override
        public String getMessage() {
            return this.shadow.getMessage();
        }

        @Override
        public String getLocalizedMessage() {
            return this.shadow.getLocalizedMessage();
        }

        @Override
        public Throwable getCause() {
            Throwable cause = this.shadow.getCause();
            return cause == null ? null : new MutedException(cause);
        }
    }
}
