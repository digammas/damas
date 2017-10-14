package solutions.digamma.damas.jcr.error;

import solutions.digamma.damas.AuthenticationException;
import solutions.digamma.damas.AuthorizationException;
import solutions.digamma.damas.ConflictException;
import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.InternalStateException;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.MisuseException;
import solutions.digamma.damas.inspection.NotNull;

import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.LoginException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.security.AccessControlException;

/**
 * JCR internal exception.
 *
 * @author Ahmad Shahwan
 */
public class JcrException extends MisuseException {

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
    public static WorkspaceException of(
            @NotNull Exception e) {
        if (e instanceof WorkspaceException) {
            return (WorkspaceException) e;
        }
        if (e instanceof RepositoryException) {
            return JcrException.of((RepositoryException) e);
        }
        return new JcrException(e);
    }

    @NotNull
    public static WorkspaceException of(
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
        if (e instanceof ConstraintViolationException) {
            return new InternalStateException(e);
        }
        return new JcrException(e);
    }
}
