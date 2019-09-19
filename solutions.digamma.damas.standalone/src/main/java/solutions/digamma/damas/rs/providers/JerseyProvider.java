package solutions.digamma.damas.rs.providers;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;
import solutions.digamma.damas.logging.Logbook;

/**
 * Jersey JAX-RS implementation provider, with Grizzly HTTP server.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JerseyProvider {

    @Inject @Configuration("http.port") @Fallback("8080")
    private Integer port;

    @Inject
    private Instance<Application> applications;

    @Inject
    private Logbook logger;

    private HttpServer server;

    @PostConstruct
    void init() {
        boolean found = this.applications
                .stream()
                .map(this::register)
                .reduce(Boolean::logicalOr)
                .orElse(false);
        if (!found) {
            this.logger.sever("No Web applications with a context path found.");
            return;
        }
        CLStaticHttpHandler docHandler = new CLStaticHttpHandler(
                this.getClass().getClassLoader(), "apidocs/");
        this.server.getServerConfiguration().addHttpHandler(docHandler, "/docs");
        logger.info("Starting HTTP server.");
        try {
            this.server.start();
        } catch (IOException e) {
            this.logger.sever(
                    e, "Couldn't start HTTP server on port %d.", this.port);
        }
        logger.info("HTTP server started on port %d.", this.port);
    }

    boolean register(Application application) {
        ApplicationPath annotation = application
                .getClass()
                .getAnnotation(ApplicationPath.class);
        if (annotation == null) {
            return false;
        }
        HttpHandler restHandler = RuntimeDelegate.getInstance()
                .createEndpoint(application, HttpHandler.class);
        this.server = HttpServer.createSimpleServer(null, this.port);
        String path = annotation.value();
        String mapping = path.startsWith("/") ? path : "/".concat(path);
        this.server.getServerConfiguration()
                .addHttpHandler(restHandler, mapping);
        return true;
    }

    @PreDestroy
    void dispose() {
        if (this.server != null) {
            logger.info("Shutting down HTTP server.");
            this.server.shutdown();
        }
    }
}
