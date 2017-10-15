package solutions.digamma.damas.common;

/**
 * Exception thrown when item or resource is not found.
 *
 * @author Ahmad Shahwan
 */
public class NotFoundException extends WorkspaceException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Exception e) {
        super(e);
    }

    public NotFoundException(String message, Exception e) {
        super(message, e);
    }
}
