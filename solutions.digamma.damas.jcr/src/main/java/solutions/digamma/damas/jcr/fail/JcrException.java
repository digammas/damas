package solutions.digamma.damas.jcr.fail;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.inspection.Nonnull;

import javax.jcr.RepositoryException;

/**
 * JCR internal exception.
 *
 * @author Ahmad Shahwan
 */
public class JcrException extends DocumentException {

    public JcrException() {
    }

    public JcrException(String message) {
        super(message);
    }

    public JcrException(Exception e) {
        super(e);
    }

    public JcrException(String message, Exception e) {
        super(message, e);
    }

    @Nonnull public static DocumentException wrap(
            @Nonnull RepositoryException e) {
        return new JcrException(e);
    }

    @Nonnull public static DocumentException wrap(
            @Nonnull String message,
            @Nonnull RepositoryException e) {
        return new JcrException(message, e);
    }
}
