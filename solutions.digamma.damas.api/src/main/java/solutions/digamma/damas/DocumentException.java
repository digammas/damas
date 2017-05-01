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
        NORMAL,
        /**
         * Exception denotes a misuse or an error.
         */
        HIGH
    }

    protected Severity severity = Severity.NORMAL;

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

    /**
     * Exception severity.
     *
     * @return
     */
    public Severity getSeverity() {
        return this.severity;
    }

    /**
     * Set severity to normal.
     *
     * @return
     */
    public DocumentException alleviate() {
        this.severity = Severity.NORMAL;
        return this;
    }

    /**
     * Set exception severity to high.
     *
     * @return
     */
    public DocumentException aggravate() {
        this.severity = Severity.HIGH;
        return this;
    }
}
