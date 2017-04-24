package solutions.digamma.damas;

/**
 * Compatibility exception.
 *
 * @author Ahmad Shahwan
 */
public class SevereDocumentException extends DocumentException {

    public SevereDocumentException() {
        this.severity = Severity.SEVERE;
    }

    public SevereDocumentException(String message) {
        super(message);
        this.severity = Severity.SEVERE;
    }

    public SevereDocumentException(Exception e) {
        super(e);
        this.severity = Severity.SEVERE;
    }

    public SevereDocumentException(String message, Exception e) {
        super(message, e);
        this.severity = Severity.SEVERE;
    }
}
