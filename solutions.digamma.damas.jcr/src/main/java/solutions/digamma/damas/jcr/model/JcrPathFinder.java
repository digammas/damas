package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.PathFinder;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.error.JcrException;
import solutions.digamma.damas.jcr.session.SessionUser;
import solutions.digamma.damas.jcr.session.SessionWrapper;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * @author Ahmad Shahwan
 */
public interface JcrPathFinder<T extends Entity>
        extends SessionUser, PathFinder<T> {

    @Override
    default T find(@NotNull Token token, @NotNull  String path)
            throws DocumentException {
        try (SessionWrapper session = openSession(token)) {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (path.equals("")) {
                path = ".";
            }
            return this.find(
                    session.getSession(), path);
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
     * @throws DocumentException
     */
    T find(@NotNull Session session, String path)
            throws RepositoryException, DocumentException;
}
