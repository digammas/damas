package solutions.digamma.damas;

/**
 * Generic exception. All API method may throw an instance of this exception.
 *
 * @author Ahmad Shahwan
 */
public class DocumentException extends Exception {

    public DocumentException() {
        super();
    }

    public DocumentException(String message) {
        super(message);
    }

    public DocumentException(Exception e) {
        super(e);
    }

    public  DocumentException(String message, Exception e) {
        super(message, e);
    }
}
