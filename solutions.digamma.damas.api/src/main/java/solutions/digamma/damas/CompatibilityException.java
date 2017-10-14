package solutions.digamma.damas;

import java.util.logging.Level;

/**
 * Exception occurs in case of incompatibility between DMS items. For example
 * this exception should be thrown when an identifier of existing but not
 * compatible item is passed to a method.
 *
 * @author Ahmad Shahwan
 */
public class CompatibilityException extends MisuseException {

    public CompatibilityException() {
        super();
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
