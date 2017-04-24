package solutions.digamma.damas.rs;

import solutions.digamma.damas.*;
import solutions.digamma.damas.UnsupportedOperationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.Serializable;

/**
 * Checked exception mapper.
 *
 * @author Ahmad Shahwan
 */
@Provider
public class DocumentExceptionMapper
        implements ExceptionMapper<DocumentException> {

    @Override
    public Response toResponse(DocumentException e) {
        return Response
            .status(toStatusCode(e))
            .entity(new ExceptionBox(e))
            .build();
    }

    public int toStatusCode(DocumentException e) {
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
     * Exception box, showing only relevant information.
     */
    public class ExceptionBox implements Serializable {

        private DocumentException exception;

        ExceptionBox(DocumentException e) {
            this.exception = e;
        }

        /**
         * Wrapped exception message.
         *
         * @return
         */
        public String getMessage() {
            return this.exception.getMessage();
        }
    }
}
