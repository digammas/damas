package solutions.digamma.damas.common;

/**
 * Exception thrown when a conflict occurs.
 *
 * @author Ahmad Shahwan
 */
public class ConflictException extends WorkspaceException {

    public ConflictException() {
    }

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(Exception e) {
        super(e);
    }

    public ConflictException(String message, Exception e) {
        super(message, e);
    }
}
