package solutions.digamma.damas.common;

/**
 * Exception occurs when the DMS API is misused.
 *
 * @author Ahmad Shahwan
 */
public class MisuseException extends WorkspaceException {

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
        this.setOrigin(Origin.API);
    }
}
