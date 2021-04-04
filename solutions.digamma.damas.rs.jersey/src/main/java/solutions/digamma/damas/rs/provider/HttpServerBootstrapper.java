package solutions.digamma.damas.rs.provider;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
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
        logger.info("%d HTTP application(s) collected.", handlers.size());
        try {
            this.server = HttpServer.create();
            this.server.bind(new InetSocketAddress(this.port), 0);
        } catch (IOException e) {
            this.logger.severe(e,
                    "Error while binding http server on port %d.", this.port);
        }
        /* Add collected handlers to server
         */
        handlers.forEach(this.server::createContext);
        this.server.start();
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

    @PreDestroy
    public void dispose() {
        if (this.server != null) {
            logger.info("Shutting down HTTP server.");
            this.server.stop(0);
        }
    }
}
