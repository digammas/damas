package solutions.digamma.damas.rs.provider;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
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
 * HTTP Server bootstrapper.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class HttpServerBootstrapper {

    @Inject
    @Configuration({"http.port", "HTTP_PORT"}) @Fallback("8080")
    private Integer port;

    @Inject
    private Instance<Application> applications;

    @Inject
    private Logbook logger;

    private HttpServer server;

    /**
     * Allow eager initialization.
     *
     * @param event         application initialization event
     */
    public void onAppInitialization(
            @Observes @Initialized(ApplicationScoped.class) Object event) {
    }

    @PostConstruct
    public void init() {
        /* Only one application is expected to be found */
        boolean found = this.applications
                .stream()
                .sequential()
                .anyMatch(this::register);
        if (!found) {
            this.logger.severe("No Web applications with context path found.");
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
        try {
            this.server = HttpServer
                    .create(new InetSocketAddress(this.port), 0);
        } catch (IOException e) {
            this.logger.severe(e,
                    "Error while creating http server on port %d.", this.port);
            return false;
        }
        String path = annotation.value();
        String mapping = path.startsWith("/") ? path : "/".concat(path);
        this.server.createContext(mapping, restHandler);
        return true;
    }

    @PreDestroy
    public void dispose() {
        if (this.server != null) {
            logger.info("Shutting down HTTP server.");
            this.server.stop(0);
        }
    }
}
