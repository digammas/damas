package solutions.digamma.damas.jcr.error;

import solutions.digamma.damas.*;
import solutions.digamma.damas.inspection.NotNull;

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

    @NotNull
    public static DocumentException wrap(
            @NotNull Exception e) {
        if (e instanceof DocumentException) {
            return wrap((DocumentException) e);
        }
        if (e instanceof RepositoryException) {
            return wrap((RepositoryException) e);
        }
        return new JcrException(e);
    }

    @NotNull
    public static DocumentException wrap(
            @NotNull DocumentException e) {
        return e;
    }

    @NotNull
    public static DocumentException wrap(
            @NotNull RepositoryException e) {
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
