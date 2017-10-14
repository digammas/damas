package solutions.digamma.damas;

import java.util.logging.Level;

/**
 * Generic exception. All API method may throw an instance of this exception.
 *
 * @author Ahmad Shahwan
 */
public class DocumentException extends Exception {

    private Level logLevel = Level.INFO;

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
     * Exception logLevel.
     *
     * @return
     */
    public Level getLogLevel() {
        return this.logLevel;
    }

    protected void setLogLevel(Level value) {
        this.logLevel = value;
    }
}
