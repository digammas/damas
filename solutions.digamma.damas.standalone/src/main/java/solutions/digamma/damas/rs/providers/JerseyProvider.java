package solutions.digamma.damas.rs.providers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;
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
        try {
            this.server =
                    HttpServer.create(new InetSocketAddress(this.port), 0);
        } catch (IOException e) {
            this.logger.sever(e, "Cannot run server on port %d.", this.port);
            return;
        }
        boolean found = this.applications
                .stream()
                .map(this::register)
                .reduce(Boolean::logicalOr)
                .orElse(false);
        if (!found) {
            this.logger.sever("No Web applications with a context path found.");
            return;
        }
        logger.info("Starting HTTP server.");
        this.server.start();
        logger.info("HTTP server started on port %d.", this.port);
    }

    private boolean register(Application application) {
        ApplicationPath annotation = application
                .getClass()
                .getAnnotation(ApplicationPath.class);
        if (annotation == null) {
            return false;
        }
        HttpHandler restHandler = RuntimeDelegate.getInstance()
                .createEndpoint(application, HttpHandler.class);
        String path = annotation.value();
        String mapping = path.startsWith("/") ? path : "/".concat(path);
        this.server.createContext(mapping, restHandler);
        return true;
    }

    @PreDestroy
    void dispose() {
        if (this.server != null) {
            logger.info("Shutting down HTTP server.");
            this.server.stop(0);
        }
    }
}
