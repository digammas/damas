package solutions.digamma.damas.common;

/**
 * Generic exception. All API method may throw an instance of this exception.
 *
 * @author Ahmad Shahwan
 */
public class WorkspaceException extends Exception {

    /**
     * Exception's origin.
     */
    public enum Origin {
        /**
         * Domain exceptions happen during normal usage, and indicate a domain
         * specific error (access denied, requested document not found, etc).
         */
        DOMAIN,
        /**
         * API exceptions happen when the programming interface is misused.
         */
        API,
        /**
         * Internal exceptions happen when an unexpected error occurs.
         */
        INTERNAL
    }

    private Origin origin = Origin.DOMAIN;

    public WorkspaceException() {
        super();
    }

    public WorkspaceException(String message) {
        super(message);
    }

    public WorkspaceException(Exception e) {
        super(e);
    }

    public WorkspaceException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Exception origin.
     *
     * @return  exception's origin
     */
    public Origin getOrigin() {
        return this.origin;
    }

    public void setOrigin(Origin value) {
        this.origin = value;
    }
}
