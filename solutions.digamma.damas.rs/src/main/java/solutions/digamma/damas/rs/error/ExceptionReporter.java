package solutions.digamma.damas.rs.error;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Unchecked exception mapper.
 *
 * @author Ahmad Shahwan
 */
public class ExceptionReporter<E extends Throwable>
        implements ExceptionMapper<E> {

    protected Response.Status status;

    public ExceptionReporter(Response.Status status) {
        this.status = status;
    }

    @Override
    public Response toResponse(E e) {
        return Response
            .status(this.status)
            .entity(new ExceptionReport(e))
            .build();
    }
}
