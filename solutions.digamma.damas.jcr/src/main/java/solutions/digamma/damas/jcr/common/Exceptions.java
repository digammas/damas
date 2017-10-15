package solutions.digamma.damas.jcr.common;

import solutions.digamma.damas.common.AuthenticationException;
import solutions.digamma.damas.common.AuthorizationException;
import solutions.digamma.damas.common.ConflictException;
import solutions.digamma.damas.common.InternalStateException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.inspection.NotNull;

import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.LoginException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.security.AccessControlException;
import java.util.logging.Level;

/**
 * Repository exception conversion utility.
 *
 * This utility class converts {@link RepositoryException} objects to
 * corresponding {@link WorkspaceException} objects.
 *
 * @author Ahmad Shahwan
 */
public class Exceptions {

    @NotNull
    public static WorkspaceException convert(
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
        WorkspaceException we = new WorkspaceException(e);
        we.setLogLevel(Level.SEVERE);
        return we;
    }
}
