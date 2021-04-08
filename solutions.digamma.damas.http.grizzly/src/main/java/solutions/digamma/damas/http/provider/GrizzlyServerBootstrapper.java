package solutions.digamma.damas.http.provider;

import java.io.IOException;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import solutions.digamma.damas.http.HttpServerBootstrapper;

/**
 * HTTP Server bootstrapper.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class GrizzlyServerBootstrapper
        extends HttpServerBootstrapper<HttpHandler> {

    private HttpServer server;
    private WebappContext context = new WebappContext("Servlet Context");

    public GrizzlyServerBootstrapper() {
        super(HttpHandler.class);
    }

    @Override
    protected void register(String mapping, HttpHandler handler) {
        this.server.getServerConfiguration()
                .addHttpHandler(handler, mapping);
    }

    @Override
    protected void register(String mapping, HttpServlet servlet) {
        String name = mapping.replace("/", "_");
        this.context.addServlet(name, servlet).addMapping(mapping);
    }

    @Override
    protected void bindServer() {
        this.server = HttpServer.createSimpleServer(null, this.port);
    }

    @Override
    protected void deployServer() {
        this.context.deploy(this.server);
    }

    @Override
    protected void startServer() throws IOException {
        this.server.start();
    }

    @Override
    protected void stopServer() {
        if (this.server != null) {
            this.server.shutdown();
        }
    }
}
