package solutions.digamma.damas.rs.provider;

import java.io.IOException;
import javax.inject.Singleton;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;

/**
 * HTTP Server bootstrapper.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class GrizzlyServerBootstrapper
        extends HttpServerBootstrapper<HttpHandler> {

    private HttpServer server;

    public GrizzlyServerBootstrapper() {
        super(HttpHandler.class);
    }

    @Override
    protected void register(String mapping, HttpHandler handler) {
        this.server.getServerConfiguration()
                .addHttpHandler(handler, mapping);
    }

    @Override
    protected void prepareServer() {
        this.server = HttpServer.createSimpleServer(null, this.port);
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
