package solutions.digamma.damas.common;

/**
 * @author Ahmad Shahwan
 */
public class InvalidArgumentException extends WorkspaceException {

    public InvalidArgumentException() {
        this(null, null);
    }

    public InvalidArgumentException(String message) {
        this(message, null);
    }

    public InvalidArgumentException(Exception e) {
        this(null, e);
    }

    public InvalidArgumentException(String message, Exception e) {
        super(message, e);
    }
}
