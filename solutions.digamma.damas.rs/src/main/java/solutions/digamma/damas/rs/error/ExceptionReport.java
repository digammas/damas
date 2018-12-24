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
     * @return error message
     */
    public String getMessage() {
        return this.exception.getMessage();
    }

    /**
     * Error cause.
     *
     * @return exception cause
     */
    public ExceptionReport getCause() {
        Throwable cause = this.exception.getCause();
        return cause == null ? null : new ExceptionReport(cause);
    }
}

