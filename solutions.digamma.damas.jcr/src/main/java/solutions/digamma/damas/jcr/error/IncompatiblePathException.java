package solutions.digamma.damas.jcr.error;

import solutions.digamma.damas.CompatibilityException;

/**
 * Incompatible path exception.
 *
 * @author Ahmad Shahwan
 */
public class IncompatiblePathException extends CompatibilityException {

    public IncompatiblePathException() {
    }

    public IncompatiblePathException(String message) {
        super(message);
    }

    public IncompatiblePathException(Exception e) {
        super(e);
    }

    public IncompatiblePathException(String message, Exception e) {
        super(message, e);
    }
}
