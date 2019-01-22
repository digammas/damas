package solutions.digamma.damas.rs.log;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.rs.error.ExceptionReport;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Container filter that logs errors.
 *
 * @author Ahmad Shahwan
 */
@Provider
@PreMatching
public class ErrorLogResponseFilter implements ContainerResponseFilter {

    @Inject
    private Logger logger;

    @Override
    public void filter(
            ContainerRequestContext containerRequestContext,
            ContainerResponseContext containerResponseContext) {
        Object entity = containerResponseContext.getEntity();
        if (entity instanceof ExceptionReport) {
            log(((ExceptionReport) entity).getException());
        }
    }

    private void log(Throwable e) {
        if (e instanceof WorkspaceException) {
            log((WorkspaceException) e);
        } else {
            this.logger.log(Level.SEVERE, "Unchecked exception.", e);
        }
    }

    private void log(WorkspaceException e) {
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
