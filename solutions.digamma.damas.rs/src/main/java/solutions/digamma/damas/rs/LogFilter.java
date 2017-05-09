package solutions.digamma.damas.rs;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Ahmad Shahwan
 */
@Provider
@PreMatching
public class LogFilter
        implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    private Logger log;

    @Override
    public void filter(ContainerRequestContext containerRequestContext)
            throws IOException {
        String url = containerRequestContext
                .getUriInfo()
                .getAbsolutePath()
                .toString();
        String method = containerRequestContext.getMethod();
        this.log.info(() -> String.format(
                "Incoming request: %s %s.", method, url));
    }

    @Override
    public void filter(
            ContainerRequestContext containerRequestContext,
            ContainerResponseContext containerResponseContext)
            throws IOException {
        String url = containerRequestContext
                .getUriInfo()
                .getAbsolutePath()
                .toString();
        String method = containerRequestContext.getMethod();
        int code = containerResponseContext.getStatus();
        this.log.info(() -> String.format(
                "Response to %s %s: %d.", method, url, code));
    }
}
