package solutions.digamma.damas.rs.error;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Exception mapper that boxes an exception into a serializable report.
 *
 * @author Ahmad Shahwan
 */
public class ExceptionReporter<E extends Throwable>
        implements ExceptionMapper<E> {

    private Response.Status status;

    ExceptionReporter(Response.Status status) {
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
