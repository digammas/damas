package solutions.digamma.damas.http;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
public abstract class HttpServerBootstrapper<T> {

    @Inject
    @Configuration({"http.port", "HTTP_PORT"}) @Fallback("8080")
    protected Integer port;

    @Inject
    private Instance<Application> applications;

    @Inject
    private Instance<HttpServlet> httpServlets;

    @Inject
    private Logbook logger;

    protected Class<T> handlerClass;

    protected HttpServerBootstrapper(Class<T> klass) {
        this.handlerClass = klass;
    }

    @PostConstruct
    public void init() {
        Map<String, T> handlers;
        Map<String, HttpServlet> servlets;
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
        logger.info("%d HTTP application(s) collected.", handlers.size());
        try {
            /* Collect servlets that have path annotation.
             */
            servlets = this.httpServlets
                    .stream()
                    .sequential()
                    .filter(this::accept)
                    .collect(Collectors.toMap(this::mapping, x -> x));
        } catch (IllegalStateException e) {
            this.logger.severe("More than one servlet share same path.");
            return;
        }
        logger.info("%d HTTP servlet(s) collected.", servlets.size());
        try {
            this.bindServer();
        } catch (IOException e) {
            this.logger.severe(e,
                    "Error while binding http server on port %d.", this.port);
        }
        /* Add collected handlers to server
         */
        handlers.forEach(this::register);
        servlets.forEach(this::register);
        this.deployServer();
        try {
            this.startServer();
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

    private T handler(Application application) {
        return RuntimeDelegate
                .getInstance()
                .createEndpoint(application, this.handlerClass);
    }

    protected abstract void register(String mapping, T handler);

    private boolean accept(HttpServlet servlet) {
        WebServlet annotation = servlet
                .getClass()
                .getAnnotation(WebServlet.class);
        return annotation != null && annotation.value().length != 0;
    }

    private String mapping(HttpServlet servlet) {
        WebServlet annotation = servlet
                .getClass()
                .getAnnotation(WebServlet.class);
        String path = annotation.value()[0];
        return path.startsWith("/") ? path : "/".concat(path);
    }

    protected abstract void register(String mapping, HttpServlet servlet);

    @PreDestroy
    public void dispose() {
        logger.info("Shutting down HTTP server.");
        this.stopServer();
    }

    protected abstract void bindServer() throws IOException;

    protected abstract void deployServer();

    protected abstract void startServer() throws IOException;

    protected abstract void stopServer();
}
