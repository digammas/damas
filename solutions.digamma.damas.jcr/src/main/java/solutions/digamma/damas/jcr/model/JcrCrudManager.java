package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.error.JcrExceptionMapper;
import solutions.digamma.damas.jcr.session.SessionWrapper;
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
    public T create(@NotNull Token token, @NotNull T entity)
            throws DocumentException {
        try (SessionWrapper session = openSession(token)) {
            return this.create(session.getSession(), entity);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Logged
    @Override
    public T update(@NotNull Token token, @NotNull String id, @NotNull T entity)
            throws DocumentException {
        try (SessionWrapper session = openSession(token)) {
            return this.update(session.getSession(), id, entity);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Logged
    @Override
    public void delete(@NotNull Token token, @NotNull String id)
            throws DocumentException {
        try (SessionWrapper session = openSession(token)) {
            this.delete(session.getSession(), id);
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
            @NotNull Session session, @NotNull T entity)
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
            @NotNull Session session,
            @NotNull String id,
            @NotNull T entity)
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
            @NotNull Session session, @NotNull String id)
            throws RepositoryException, DocumentException;
}
