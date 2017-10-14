package solutions.digamma.damas;

import java.util.logging.Level;

/**
 * Generic exception. All API method may throw an instance of this exception.
 *
 * @author Ahmad Shahwan
 */
public class WorkspaceException extends Exception {

    private Level logLevel = Level.INFO;

    public WorkspaceException() {
        super();
    }

    public WorkspaceException(String message) {
        super(message);
    }

    public WorkspaceException(Exception e) {
        super(e);
    }

    public WorkspaceException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Exception logLevel.
     *
     * @return
     */
    public Level getLogLevel() {
        return this.logLevel;
    }

    protected void setLogLevel(Level value) {
        this.logLevel = value;
    }
}
