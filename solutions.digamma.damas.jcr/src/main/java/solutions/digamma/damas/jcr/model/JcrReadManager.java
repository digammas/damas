package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.EntityManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.session.SessionWrapper;
import solutions.digamma.damas.jcr.error.JcrExceptionMapper;
import solutions.digamma.damas.logging.Logged;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Abstract read manager.
 *
 * @author Ahmad Shahwan
 */
abstract public class JcrReadManager<T extends Entity>
        extends JcrManager implements EntityManager<T> {

    @Logged
    @Override
    public T retrieve(@NotNull Token token, @NotNull String id)
            throws DocumentException {
        try (SessionWrapper session = openSession(token)) {
            return this.retrieve(session.getSession(), id);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    /**
     * Perform retrieval.
     *
     * @param session
     * @param id
     * @return
     * @throws RepositoryException
     * @throws DocumentException
     */
    abstract protected T retrieve(@NotNull Session session, @NotNull String id)
            throws RepositoryException, DocumentException;
}
