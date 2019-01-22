package solutions.digamma.damas.rs.log;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Container filter that logs incoming requests.
 *
 * @author Ahmad Shahwan
 */
@Provider
@PreMatching
public class LogRequestFilter implements ContainerRequestFilter {

    @Inject
    private Logger log;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String url = containerRequestContext
                .getUriInfo()
                .getAbsolutePath()
                .toString();
        String method = containerRequestContext.getMethod();
        this.log.info(() -> String.format(
                "Incoming request: %s %s.", method, url));
    }
}
