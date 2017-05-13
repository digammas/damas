package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.jcr.error.JcrExceptionMapper;
import solutions.digamma.damas.jcr.session.UserSession;
import solutions.digamma.damas.logging.Logged;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * JCR CRUD manager.
 *
 * @author Ahmad Shahwan
 */
abstract public class JcrCrudManager<T extends Entity>
        extends JcrReadManager<T> implements CrudManager<T> {

    @Logged
    @Override
    public T create(@Nonnull Token token, @Nonnull T entity)
            throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            return this.create(session.toJcrSession(), entity);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Logged
    @Override
    public T update(@Nonnull Token token, @Nonnull String id, @Nonnull T entity)
            throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            return this.update(session.toJcrSession(), id, entity);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Logged
    @Override
    public void delete(@Nonnull Token token, @Nonnull String id)
            throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            this.delete(session.toJcrSession(), id);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    /**
     * Perform creation.
     *
     * @param session
     * @param entity
     * @return
     * @throws RepositoryException
     * @throws DocumentException
     */
    abstract protected T create(
            @Nonnull Session session, @Nonnull T entity)
            throws RepositoryException, DocumentException;

    /**
     * Perform update.
     *
     * @param session
     * @param id
     * @param entity
     * @return
     * @throws RepositoryException
     * @throws DocumentException
     */
    abstract protected T update(
            @Nonnull Session session,
            @Nonnull String id,
            @Nonnull T entity)
            throws RepositoryException, DocumentException;

    /**
     * Perform deletion.
     *
     * @param session
     * @param id
     * @throws RepositoryException
     * @throws DocumentException
     */
    abstract protected void delete(
            @Nonnull Session session, @Nonnull String id)
            throws RepositoryException, DocumentException;

}
