package solutions.digamma.damas;

/**
 * Compatibility exception.
 *
 * @author Ahmad Shahwan
 */
public class CompatibilityException extends SevereDocumentException {

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
