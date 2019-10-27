package solutions.digamma.damas.logging;

import java.util.logging.Handler;
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
        this(name, null);
    }

    /**
     * Constructor with name and collection of handlers.
     *
     * @param name      Logger name.
     * @param handlers  Iterable of log handlers.
     */
    Logbook(String name, Iterable<Handler> handlers) {
        super(name, null);
        if (handlers != null) {
            handlers.forEach(this::addHandler);
        }
    }

    public void info(String format, Object... args) {
        this.info(() -> String.format(format, args));
    }

    public void severe(String format, Object... args) {
        this.severe(() -> String.format(format, args));
    }

    public void severe(Exception e, String format, Object... args) {
        this.log(Level.SEVERE, e, () -> String.format(format, args));
    }
}
