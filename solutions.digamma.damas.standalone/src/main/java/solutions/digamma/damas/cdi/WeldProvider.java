package solutions.digamma.damas.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import solutions.digamma.damas.rs.providers.JerseyProvider;

/**
 * Application entry point.
 *
 * @author Ahmad Shahwan
 */
public class WeldProvider {

    public void run() {
        try (WeldContainer weld = new Weld().initialize()) {
            if (weld.select(JerseyProvider.class).get() == null) {
                System.err.println("No REST provider found.");
            } else {
                Thread.currentThread().join();
            }
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] argv) {
        new WeldProvider().run();
    }
}
