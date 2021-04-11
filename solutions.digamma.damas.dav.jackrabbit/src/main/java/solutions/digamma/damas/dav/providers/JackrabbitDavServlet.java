package solutions.digamma.damas.dav.providers;

import java.io.IOException;
import javax.inject.Inject;
import javax.jcr.Repository;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jackrabbit.webdav.simple.SimpleWebdavServlet;

/**
 * Basic WebDav support.
 */
@WebServlet(JackrabbitDavServlet.URL_BASE)
public class JackrabbitDavServlet extends SimpleWebdavServlet {

    public static final String URL_BASE = "webdav";

    @Inject
    private Repository repository;

    @Override
    public Repository getRepository() {
        return this.repository;
    }

    private static String expectedPrefix = String.join(
            "/", "", URL_BASE, "default", "content");

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    public String getInitParameter(String name) {
        if ("resource-path-prefix".equals(name)) {
            return "/".concat(URL_BASE);
        }
        return super.getInitParameter(name);
    }

    @Override
    protected void service(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        /* Disallow access to non-public content */
        if (!isPublic(request.getRequestURI())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        super.service(request, response);
    }

    private static boolean isPublic(String uri) {
        return expectedPrefix.equals(uri) ||
                uri.startsWith(expectedPrefix.concat("/"));
    }
}
