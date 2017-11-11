package solutions.digamma.damas.common;

/**
 * Unsupported operation exception.
 *
 * @author Ahmad Shahwan
 */
public class UnsupportedActionException extends MisuseException {

    public UnsupportedActionException() {
        super();
    }

    public UnsupportedActionException(String message) {
        super(message);
    }

    public UnsupportedActionException(Exception e) {
        super(e);
    }

    public UnsupportedActionException(String message, Exception e) {
        super(message, e);
    }
}
