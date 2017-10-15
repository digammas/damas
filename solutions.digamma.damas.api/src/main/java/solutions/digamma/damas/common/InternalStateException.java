package solutions.digamma.damas.common;

import java.util.logging.Level;

/**
 * Exception thrown when internal state is incompatible with requested
 * operation. Such an exception usual denotes an anomaly and should be fully
 * logged.
 *
 * @author Ahmad Shahwan
 */
public class InternalStateException extends WorkspaceException {

    public InternalStateException() {
        this(null, null);
    }

    public InternalStateException(String message) {
        this(message, null);
    }

    public InternalStateException(Exception e) {
        this(null, e);
    }

    public InternalStateException(String message, Exception e) {
        super(message, e);
        this.setLogLevel(Level.SEVERE);
    }
}