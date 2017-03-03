package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.FullManager;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.auth.UserSession;
import solutions.digamma.damas.jcr.fail.JcrException;

import javax.jcr.RepositoryException;

/**
 * JCR full manager.
 *
 * @author Ahmad Shahwan
 */
abstract public class JcrFullManager<T extends Entity>
    extends JcrCrudManager<T> implements FullManager<T> {

    @Override
    public Page<T> find(@Nonnull Token token) throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            return this.find(session);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    @Override
    public Page<T> find(
            @Nonnull Token token, int offset, int size)
            throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            return this.find(session, offset, size);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    @Override
    public Page<T> find(
            @Nonnull Token token, int offset, int size, @Nullable Object query)
            throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            return this.find(session, offset, size, query);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    /**
     * Perform all-record retrieval.
     *
     * @param session
     * @return
     * @throws RepositoryException
     * @throws DocumentException
     */
    abstract public Page<T> find(@Nonnull UserSession session)
        throws RepositoryException, DocumentException;

    /**
     * Perform search.
     *
     * @param session
     * @param offset
     * @param size
     * @return
     * @throws RepositoryException
     * @throws DocumentException
     */
    abstract public Page<T> find(
            @Nonnull UserSession session, int offset, int size)
            throws RepositoryException, DocumentException;

    /**
     * Perform search.
     *
     * @param session
     * @param offset
     * @param size
     * @param query
     * @return
     * @throws RepositoryException
     * @throws DocumentException
     */
    abstract public Page<T> find(
            @Nonnull UserSession session,
            int offset,
            int size,
            @Nullable Object query)
            throws RepositoryException, DocumentException;
}
