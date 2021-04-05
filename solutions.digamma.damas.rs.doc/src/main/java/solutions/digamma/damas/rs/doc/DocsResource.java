package solutions.digamma.damas.rs.doc;

import java.io.InputStream;
import java.nio.file.Paths;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * This resource exposes generated documentations as an endpoint.
 */
@Path("")
public class DocsResource {

    private static final String DOC_BASE = "apidocs";
    private static final String INDEX_HTML = "index.html";

    private final ClassLoader cl = this.getClass().getClassLoader();

    @GET
    @Path("{path: .*}")
    public Response staticResources(@PathParam("path") final String path) {
        String location = this.getLocation(path);
        InputStream resource = this.cl.getResourceAsStream(location);
        return (resource == null ?
                Response.status(Response.Status.NOT_FOUND) :
                Response.ok().entity(resource)
        ).build();
    }

    private String getLocation(String path) {
        String location = Paths.get(DOC_BASE, path).toString();
        if (path.endsWith("/")) {
            return location.concat(INDEX_HTML);
        }
        /* Check if resource is directory */
        if (this.cl.getResource(location.concat("/")) != null) {
            return location.concat("/").concat(INDEX_HTML);
        }
        return location;
    }
}
