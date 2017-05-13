package solutions.digamma.damas.jcr.error;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.inspection.Nonnull;

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

    @Nonnull public static DocumentException map(
            @Nonnull Exception e) {
        if (e instanceof DocumentException) {
            return map((DocumentException) e);
        }
        if (e instanceof RepositoryException) {
            return map((RepositoryException) e);
        }
        return new JcrException(e);
    }

    @Nonnull public static DocumentException map(
            @Nonnull DocumentException e) {
        return e;
    }

    @Nonnull public static DocumentException map(
            @Nonnull RepositoryException e) {
        if (e instanceof ItemNotFoundException) {
            return new NotFoundException(e);
        }
        return new JcrException(e);
    }
}
