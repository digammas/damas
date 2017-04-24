package solutions.digamma.damas;

/**
 * Unsupported operation exception.
 *
 * @author Ahmad Shahwan
 */
public class UnsupportedOperationException extends SevereDocumentException {

    public UnsupportedOperationException() {
    }

    public UnsupportedOperationException(String message) {
        super(message);
    }

    public UnsupportedOperationException(Exception e) {
        super(e);
    }

    public UnsupportedOperationException(String message, Exception e) {
        super(message, e);
    }
}
