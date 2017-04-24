package solutions.digamma.damas.rs.providers;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;
import solutions.digamma.damas.logging.Logbook;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.net.URI;

/**
 * Jersey JAX-RS implementation provider, with Grizzly HTTP server.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JerseyProvider {

    @Inject @Configuration("http.port") @Fallback("8080")
    private Integer port;

    @Inject @Configuration("http.path") @Fallback("dms")
    private Integer path;

    @Inject
    private Application application;

    @Inject
    private Logbook logger;

    private HttpServer server;

    @PostConstruct
    void init() {
        URI url = URI.create(String.format(
                "http://localhost:%d/%s/", this.path, this.port));
        this.server = GrizzlyHttpServerFactory.createHttpServer(
                url, ResourceConfig.forApplication(this.application), false);
        logger.info("Starting HTTP server.");
        try {
            this.server.start();
        } catch (IOException e) {
            this.logger.sever(
                    e, "Couldn't start HTTP server on port %d.", this.port);
        }
        logger.info("HTTP server started on port %d.", this.port);
    }

    @PreDestroy
    void dispose() {
        if (this.server != null) {
            logger.info("Shutting down HTTP server.");
            this.server.shutdown();
        }
    }
}
