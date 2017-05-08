package solutions.digamma.damas.rs;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Jersey JAX-RS implementation provider, with Grizzly HTTP server.
 *
 * @author Ahmad Shahwan
 */
public class JerseyProvider {

    public static final Integer PORT = 8080;
    public static final String PATH = "dms";
    public static final String URL = String.format(
            "http://localhost:%d/%s/", PORT, PATH);

    @Inject
    private Application application;

    @Inject
    private Logger logger;

    private HttpServer server;

    @PostConstruct
    void init() {
        URI url = URI.create(URL);
        this.server = GrizzlyHttpServerFactory.createHttpServer(
                url, ResourceConfig.forApplication(this.application), false);
        logger.info("Starting HTTP server.");
        try {
            this.server.start();
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, e, () -> String.format(
                    "Couldn't start HTTP server on PORT %d.", this.PORT));
        }
        logger.info(() -> String.format(
                "HTTP server started on PORT %d.", this.PORT));
    }

    @PreDestroy
    void dispose() {
        if (this.server != null) {
            logger.info("Shutting down HTTP server.");
            this.server.shutdown();
        }
    }
}
