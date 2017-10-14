package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.MisuseException;
import solutions.digamma.damas.PathFinder;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.error.JcrException;
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
            throw JcrException.of(e);
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
