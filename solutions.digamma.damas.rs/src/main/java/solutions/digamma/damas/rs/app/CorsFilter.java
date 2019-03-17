package solutions.digamma.damas.rs.app;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Filter to allow CORS request.
 */
@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    /**
     * Shortcut response to pre-flight requests.
     *
     * Proper headers will be added to the response, if relevant, with the overload of this method.
     *
     * @param request   HTTP request
     */
    @Override
    public void filter(ContainerRequestContext request) {
        if (hasOrigin(request) && "OPTIONS".equalsIgnoreCase(request.getMethod())) {
            request.abortWith(Response.ok().build());
        }
    }

    /**
     * Add relevant CROS headers to response.
     *
     * @param request   HTTP request
     * @param response  HTTP response
     */
    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {
        if (!hasOrigin(request)) {
            return;
        }
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            response.getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
        }
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
    }

    private static boolean hasOrigin(ContainerRequestContext request) {
        return request.getHeaderString("Origin") != null;
    }
}