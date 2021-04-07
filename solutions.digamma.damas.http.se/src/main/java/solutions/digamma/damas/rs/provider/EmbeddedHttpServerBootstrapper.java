package solutions.digamma.damas.rs.provider;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import javax.inject.Singleton;

/**
 * HTTP Server bootstrapper.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class EmbeddedHttpServerBootstrapper
        extends HttpServerBootstrapper<HttpHandler> {

    private HttpServer server;

    public EmbeddedHttpServerBootstrapper() {
        super(HttpHandler.class);
    }

    @Override
    protected void register(String mapping, HttpHandler handler) {
        this.server.createContext(mapping, handler);
    }

    @Override
    protected void prepareServer() throws IOException {
        this.server = HttpServer.create();
        this.server.bind(new InetSocketAddress(this.port), 0);
    }

    @Override
    protected void startServer() {
        this.server.start();
    }

    @Override
    protected void stopServer() {
        if (this.server != null) {
            this.server.stop(0);
        }
    }
}
