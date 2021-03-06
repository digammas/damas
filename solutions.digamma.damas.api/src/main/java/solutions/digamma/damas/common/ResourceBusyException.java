package solutions.digamma.damas.common;

/**
 * Exception through when concurrent access to the same resource is denied.
 *
 * @author Ahmad Shahwan
 */
public class ResourceBusyException extends WorkspaceException {

    public ResourceBusyException() {
    }

    public ResourceBusyException(String message) {
        super(message);
    }

    public ResourceBusyException(Exception e) {
        super(e);
    }

    public ResourceBusyException(String message, Exception e) {
        super(message, e);
    }
}
