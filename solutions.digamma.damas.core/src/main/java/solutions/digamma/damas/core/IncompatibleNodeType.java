package solutions.digamma.damas.core;

/**
 * Exception thrown when JCR node types are incompatible.
 *
 * @author Ahmad Shahwan
 */
public class IncompatibleNodeType extends CompatibilityException {

    public IncompatibleNodeType() {
    }

    public IncompatibleNodeType(String message) {
        super(message);
    }

    public IncompatibleNodeType(Exception e) {
        super(e);
    }

    public IncompatibleNodeType(String message, Exception e) {
        super(message, e);
    }
}
