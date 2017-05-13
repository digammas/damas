package solutions.digamma.damas.jcr.error;

import solutions.digamma.damas.CompatibilityException;

/**
 * Exception thrown when JCR node types are incompatible.
 *
 * @author Ahmad Shahwan
 */
public class IncompatibleNodeTypeException extends CompatibilityException {

    public IncompatibleNodeTypeException() {
    }

    public IncompatibleNodeTypeException(String message) {
        super(message);
    }

    public IncompatibleNodeTypeException(Exception e) {
        super(e);
    }

    public IncompatibleNodeTypeException(String message, Exception e) {
        super(message, e);
    }
}
