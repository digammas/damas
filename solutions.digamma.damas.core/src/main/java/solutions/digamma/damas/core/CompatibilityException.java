package solutions.digamma.damas.core;

import solutions.digamma.damas.api.DocumentException;

/**
 * Compatibility exception.
 *
 * @author Ahmad Shahwan
 */
public class CompatibilityException extends DocumentException {

    public CompatibilityException() {
    }

    public CompatibilityException(String message) {
        super(message);
    }

    public CompatibilityException(Exception e) {
        super(e);
    }

    public CompatibilityException(String message, Exception e) {
        super(message, e);
    }
}
