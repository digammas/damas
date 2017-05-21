package solutions.digamma.damas.jcr.error;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.inspection.NotNull;

import javax.jcr.ItemNotFoundException;
import javax.jcr.RepositoryException;

/**
 * JCR internal exception.
 *
 * @author Ahmad Shahwan
 */
public class JcrExceptionMapper {

    public JcrExceptionMapper() {
    }

    @NotNull
    public static DocumentException map(
            @NotNull Exception e) {
        if (e instanceof DocumentException) {
            return map((DocumentException) e);
        }
        if (e instanceof RepositoryException) {
            return map((RepositoryException) e);
        }
        return new JcrException(e);
    }

    @NotNull
    public static DocumentException map(
            @NotNull DocumentException e) {
        return e;
    }

    @NotNull
    public static DocumentException map(
            @NotNull RepositoryException e) {
        if (e instanceof ItemNotFoundException) {
            return new NotFoundException(e);
        }
        return new JcrException(e);
    }
}
