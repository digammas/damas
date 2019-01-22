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
public class WorkspaceExceptionMapper<E extends WorkspaceException>
        extends GenericExceptionMapper<E> {

    public WorkspaceExceptionMapper(Response.Status status) {
        super(status);
    }

    @Override
    protected void log(E e) {
        if (this.logger == null) {
            return;
        }
        switch (e.getOrigin()) {
            case API:
                this.logger.log(Level.WARNING, e.getMessage());
                break;
            case DOMAIN:
                this.logger.log(Level.INFO, e.getMessage());
                break;
            default:
                this.logger.log(Level.SEVERE, "Checked exception.", e);
        }
    }
}
