package solutions.digamma.damas;

/**
 * Exception thrown when bad, or no, authentication information found.
 *
 * @author Ahmad Shahwan
 */
public class AuthenticationException extends DocumentException {

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
