package solutions.digamma.damas.rs.error;

import solutions.digamma.damas.common.AuthenticationException;
import solutions.digamma.damas.common.AuthorizationException;
import solutions.digamma.damas.common.ConflictException;
import solutions.digamma.damas.common.InvalidArgumentException;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.common.ResourceBusyException;
import solutions.digamma.damas.common.UnsupportedActionException;

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
@Provider
public class GenericExceptionMapper
        implements ExceptionMapper<Throwable> {

    @Inject
    private Logger logger;

    @Override
    public Response toResponse(Throwable e) {
        this.logger.log(Level.SEVERE, "Unchecked exception.", e);
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(new ExceptionReport(e))
            .build();
    }
}
