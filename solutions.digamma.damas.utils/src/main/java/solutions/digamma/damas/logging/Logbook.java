package solutions.digamma.damas.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global logger.
 *
 * @author Ahmad Shahwan
 */
public class Logbook extends Logger {

    /**
     * Constructor with name.
     *
     * @param name Logger name.
     */
    public Logbook(String name) {
        super(name, null);
        this.init();
    }

    /**
     * Wrapper constructor.
     *
     * @param original original logger.
     */
    public Logbook(Logger original) {
        super(original.getName(), original.getResourceBundleName());
        this.init();
    }

    public void info(String format, Object... args) {
        this.info(() -> String.format(format, args));
    }

    public void sever(String format, Object... args) {
        this.severe(() -> String.format(format, args));
    }

    public void sever(Exception e, String format, Object... args) {
        this.log(Level.SEVERE, e, () -> String.format(format, args));
    }

    protected void init() {
        this.addHandler(new ConsoleHandler());
    }
}
