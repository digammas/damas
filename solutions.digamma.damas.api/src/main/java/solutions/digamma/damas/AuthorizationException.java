package solutions.digamma.damas;

/**
 * Exception thrown when authentication information are correct, but session
 * doesn't have sufficient rights.
 *
 * @author Ahmad Shahwan
 */
public class AuthorizationException extends WorkspaceException {

    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(Exception e) {
        super(e);
    }

    public AuthorizationException(String message, Exception e) {
        super(message, e);
    }
}
