package solutions.digamma.damas;

/**
 * Generic exception. All API method may throw an instance of this exception.
 *
 * @author Ahmad Shahwan
 */
public class DocumentException extends Exception {

    /**
     * Exception severity.
     */
    public enum Severity {
        /**
         * Exception may occur with normal usage.
         */
        OK,
        /**
         * Exception denotes a misuse or an error.
         */
        SEVERE
    }

    protected Severity severity = Severity.OK;

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

    public void ok() {
        this.severity = Severity.OK;
    }

    public void sever() {
        this.severity = Severity.SEVERE;
    }
}
