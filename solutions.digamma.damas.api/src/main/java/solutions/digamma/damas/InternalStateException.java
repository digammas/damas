package solutions.digamma.damas;

/**
 * Exception thrown when internal state is incompatible with requested
 * operation.
 *
 * @author Ahmad Shahwan
 */
public class InternalStateException extends SevereDocumentException {

    public InternalStateException() {
    }

    public InternalStateException(String message) {
        super(message);
    }

    public InternalStateException(Exception e) {
        super(e);
    }

    public InternalStateException(String message, Exception e) {
        super(message, e);
    }
}
