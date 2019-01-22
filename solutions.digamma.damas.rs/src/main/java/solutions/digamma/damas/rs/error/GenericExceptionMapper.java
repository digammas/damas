package solutions.digamma.damas.rs.error;

import solutions.digamma.damas.common.AuthenticationException;
import solutions.digamma.damas.common.AuthorizationException;
import solutions.digamma.damas.common.ConflictException;
import solutions.digamma.damas.common.InvalidArgumentException;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.common.ResourceBusyException;
import solutions.digamma.damas.common.UnsupportedActionException;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Unchecked exception mapper.
 *
 * @author Ahmad Shahwan
 */
public class GenericExceptionMapper<E extends Throwable>
        implements ExceptionMapper<E> {

    protected Response.Status status;
    protected Logger logger = Logger.getLogger(this.getClass().getName());

    public GenericExceptionMapper(Response.Status status) {
        this.status = status;
    }

    public GenericExceptionMapper() {
        this(Response.Status.INTERNAL_SERVER_ERROR);
    }


    @Override
    public Response toResponse(E e) {
        log(e);
        return Response
            .status(this.status)
            .entity(new ExceptionReport(e))
            .build();
    }

    protected void log(E e) {
        if (this.logger != null) {
            this.logger.log(Level.SEVERE, "Unchecked exception.", e);
        }
    }
}
