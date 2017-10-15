package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.common.MisuseException;
import solutions.digamma.damas.content.PathFinder;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.common.Exceptions;
import solutions.digamma.damas.jcr.session.SessionUser;
import solutions.digamma.damas.jcr.session.SessionWrapper;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.nio.file.Paths;

/**
 * @author Ahmad Shahwan
 */
public interface JcrPathFinder<T extends Entity>
        extends SessionUser, PathFinder<T> {

    @Override
    default T find(@NotNull Token token, @NotNull  String path)
            throws WorkspaceException {
        try (SessionWrapper session = openSession(token)) {
            path = Paths.get(path).normalize().toString();
            if (path.startsWith("../")) {
                throw new MisuseException("Invalid relative path.");
            }
            path = path.equals("") ? "." : path;
            return this.find(session.getSession(), path);
        } catch (RepositoryException e) {
            throw Exceptions.convert(e);
        }
    }

    /**
     * Perform path look-up.
     *
     * @param session
     * @param path
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    T find(@NotNull Session session, String path)
            throws RepositoryException, WorkspaceException;
}
