package solutions.digamma.damas.rs.provider;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
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
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;
import solutions.digamma.damas.logging.Logbook;

/**
 * HTTP Server bootstrapper.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class GrizzlyServerBootstrapper {

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
        Map<String, HttpHandler> handlers;
        try {
            /* Collect applications that have path annotation as a path-handler
             * pairs.
             */
            handlers = this.applications
                    .stream()
                    .sequential()
                    .filter(this::accept)
                    .collect(Collectors.toMap(this::mapping, this::handler));
        } catch (IllegalStateException e) {
            this.logger.severe("More than one application share same path.");
            return;
        }
        if (handlers.isEmpty()) {
            this.logger.severe("No Web applications with context path found.");
            return;
        }
        this.server = HttpServer.createSimpleServer(null, this.port);
        handlers.forEach(this::register);
        logger.info("Starting HTTP server.");
        try {
            this.server.start();
        } catch (IOException e) {
            this.logger.severe(
                    e, "Couldn't start HTTP server on port %d.", this.port);
        }
        logger.info("HTTP server started on port %d.", this.port);
    }

    private boolean accept(Application application) {
        return null != application
                .getClass()
                .getAnnotation(ApplicationPath.class);
    }

    private String mapping(Application application) {
        ApplicationPath annotation = application
                .getClass()
                .getAnnotation(ApplicationPath.class);
        String path = annotation.value();
        return path.startsWith("/") ? path : "/".concat(path);
    }

    private HttpHandler handler(Application application) {
        return RuntimeDelegate
                .getInstance()
                .createEndpoint(application, HttpHandler.class);
    }

    private void register(String mapping, HttpHandler handler) {
        this.server.getServerConfiguration()
                .addHttpHandler(handler, mapping);
    }

    @PreDestroy
    public void dispose() {
        if (this.server != null) {
            logger.info("Shutting down HTTP server.");
            this.server.shutdown();
        }
    }
}
