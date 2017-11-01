package solutions.digamma.damas.common;

/**
 * Exception thrown when the provided argument is invalid.
 *
 * Examples are insecure password, or invalid email address.
 *
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
