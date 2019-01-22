package solutions.digamma.damas.rs.error;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import javax.json.bind.annotation.JsonbTransient;

/**
 * Exception report.
 */
public class ExceptionReport implements Serializable {

    private final Throwable exception;

    /**
     * Package-protected constructor.
     *
     * @param e The exception to report.
     */
    ExceptionReport(Throwable e) {
        this.exception = e;
    }

    /**
     * Error message.
     *
     * @return Error message.
     */
    public String getMessage() {
        return this.exception.getMessage();
    }

    /**
     * Cause exception report, if any, or null when no cause.
     *
     * @return Cause exception report.
     */
    public ExceptionReport getCause() {
        Throwable cause = this.exception.getCause();
        return cause == null ? null : new ExceptionReport(cause);
    }

    /**
     * Original exception.
     *
     * @return reported exception
     */
    @XmlTransient
    @JsonbTransient
    public Throwable getException() {
        return this.exception;
    }
}

