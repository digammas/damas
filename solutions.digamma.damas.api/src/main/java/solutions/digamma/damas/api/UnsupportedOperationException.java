package solutions.digamma.damas.api;

import solutions.digamma.damas.api.DocumentException;

/**
 * Unsupported operation exception.
 *
 * @author Ahmad Shahwan
 */
public class UnsupportedOperationException extends DocumentException {

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
