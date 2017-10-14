package solutions.digamma.damas;

import java.util.logging.Level;

/**
 * Exception occurs when the DMS API is misused.
 *
 * @author Ahmad Shahwan
 */
public class MisuseException extends DocumentException {

    public MisuseException() {
        this(null, null);
    }

    public MisuseException(String message) {
        this(message, null);
    }

    public MisuseException(Exception e) {
        this(null, e);
    }

    public MisuseException(String message, Exception e) {
        super(message, e);
        this.setLogLevel(Level.WARNING);
    }
}
