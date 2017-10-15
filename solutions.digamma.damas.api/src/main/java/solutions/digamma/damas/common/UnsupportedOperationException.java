package solutions.digamma.damas.common;

/**
 * Unsupported operation exception.
 *
 * @author Ahmad Shahwan
 */
public class UnsupportedOperationException extends MisuseException {

    public UnsupportedOperationException() {
        super();
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
