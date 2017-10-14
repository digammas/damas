package solutions.digamma.damas.rs.error;

import java.io.Serializable;

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
     * @return
     */
    public String getMessage() {
        return this.exception.getMessage();
    }

    /**
     * Error cause.
     *
     * @return
     */
    public ExceptionReport getCause() {
        Throwable cause = this.exception.getCause();
        return cause == null ? null : new ExceptionReport(cause);
    }
}

