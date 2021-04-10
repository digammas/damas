package solutions.digamma.damas.standalone;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import solutions.digamma.damas.http.HttpServerBootstrapper;
import solutions.digamma.damas.logging.Logbook;

/**
 * Application launcher.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class Launcher {

    @Inject
    private Logbook logger;

    /**
     * Concurrency injection.
     */
    @Inject
    private HttpServerBootstrapper<?> httpServerBootstrapper;

    @PostConstruct
    private void init() {
        this.logger.info("Container running.");
    }

    @PreDestroy
    private void dispose() {
        this.logger.info("Container stopped.");
    }

    private void run() {
        try (WeldContainer weld = new Weld().initialize()) {
            weld.select(this.getClass()).get();
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] argv) {
        new Launcher().run();
    }
}
