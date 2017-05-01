package solutions.digamma.damas;

/**
 * Compatibility exception.
 *
 * @author Ahmad Shahwan
 */
public class SevereDocumentException extends DocumentException {

    public SevereDocumentException() {
        this.severity = Severity.HIGH;
    }

    public SevereDocumentException(String message) {
        super(message);
        this.severity = Severity.HIGH;
    }

    public SevereDocumentException(Exception e) {
        super(e);
        this.severity = Severity.HIGH;
    }

    public SevereDocumentException(String message, Exception e) {
        super(message, e);
        this.severity = Severity.HIGH;
    }
}
