package solutions.digamma.damas.common;

/**
 * Exception thrown when bad, or no, authentication information found.
 *
 * @author Ahmad Shahwan
 */
public class AuthenticationException extends WorkspaceException {

    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Exception e) {
        super(e);
    }

    public AuthenticationException(String message, Exception e) {
        super(message, e);
    }
}
