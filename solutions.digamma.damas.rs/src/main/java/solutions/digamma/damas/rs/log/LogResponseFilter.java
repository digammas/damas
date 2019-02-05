package solutions.digamma.damas.rs.log;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

/**
 * Container filter that logs outgoing responses.
 *
 * @author Ahmad Shahwan
 */
@Provider
@PreMatching
public class LogResponseFilter implements ContainerResponseFilter {

    @Inject
    private Logger log;

    @Override
    public void filter(
            ContainerRequestContext containerRequestContext,
            ContainerResponseContext containerResponseContext) {
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
