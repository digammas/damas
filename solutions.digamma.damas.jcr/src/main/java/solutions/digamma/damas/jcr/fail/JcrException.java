package solutions.digamma.damas.jcr.fail;

import solutions.digamma.damas.*;
import solutions.digamma.damas.inspection.Nonnull;

import javax.jcr.*;
import javax.jcr.security.AccessControlException;

/**
 * JCR internal exception.
 *
 * @author Ahmad Shahwan
 */
public class JcrException extends SevereDocumentException {

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
            @Nonnull Exception e) {
        if (e instanceof DocumentException) {
            return wrap((DocumentException) e);
        }
        if (e instanceof RepositoryException) {
            return wrap((RepositoryException) e);
        }
        return new JcrException(e);
    }

    @Nonnull public static DocumentException wrap(
            @Nonnull DocumentException e) {
        return e;
    }

    @Nonnull public static DocumentException wrap(
            @Nonnull RepositoryException e) {
        if (e instanceof AccessControlException) {
            return new AuthorizationException(e);
        }
        if (e instanceof ItemNotFoundException) {
            return new NotFoundException(e);
        }
        if (e instanceof ItemExistsException) {
            return new ConflictException(e);
        }
        if (e instanceof PathNotFoundException) {
            return new NotFoundException(e);
        }
        if (e instanceof LoginException) {
            return new AuthenticationException(e);
        }
        return new JcrException(e);
    }
}