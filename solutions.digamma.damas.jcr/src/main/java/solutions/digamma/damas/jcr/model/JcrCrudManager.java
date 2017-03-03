package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.jcr.auth.UserSession;
import solutions.digamma.damas.jcr.fail.JcrException;

import javax.jcr.RepositoryException;

/**
 * JCR CRUD manager.
 *
 * @author Ahmad Shahwan
 */
abstract public class JcrCrudManager<T extends Entity>
        extends JcrReadManager<T> implements CrudManager<T> {

    @Override
    public T create(@Nonnull Token token, @Nonnull T entity)
            throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            return this.create(session, entity);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    @Override
    public T update(@Nonnull Token token, @Nonnull String id, @Nonnull T entity)
            throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            return this.update(session, id, entity);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    @Override
    public void delete(@Nonnull Token token, @Nonnull String id)
            throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            this.delete(session, id);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
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
    abstract public T create(
            @Nonnull UserSession session, @Nonnull T entity)
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
    abstract public T update(
            @Nonnull UserSession session,
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
    abstract public void delete(
            @Nonnull UserSession session, @Nonnull String id)
            throws RepositoryException, DocumentException;

}
