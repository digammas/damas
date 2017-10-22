package solutions.digamma.damas.jcr.common;

/**
 * Muted exception.
 *
 * @author Ahmad Shahwan
 */
public class MutedException extends RuntimeException {

    /**
     * Cause as an {@link Exception}.
     */
    private Exception cause;

    MutedException(String message, Exception cause) {
        super(message, cause);
        this.cause = cause;
    }

    MutedException(Exception cause) {
        this("Exception muted.", cause);
    }

    @Override
    public Exception getCause() {
        return this.cause;
    }
}
