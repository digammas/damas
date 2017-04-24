package solutions.digamma.damas.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * Application entry point.
 *
 * @author Ahmad Shahwan
 */
public class WeldProvider {

    public static void main(String[] argv) {
        try (WeldContainer weld = new Weld().initialize()) {}
    }
}
