package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.PathFinder;
import solutions.digamma.damas.SearchEngine;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.session.UserSession;
import solutions.digamma.damas.jcr.error.JcrExceptionMapper;
import solutions.digamma.damas.logging.Logged;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * JCR full manager.
 *
 * @author Ahmad Shahwan
 */
abstract public class JcrFullManager<T extends Entity>
    extends JcrCrudManager<T>
    implements CrudManager<T>, SearchEngine<T>, PathFinder<T> {


    /**
     * Default result page size.
     */
    public static final int DEFAULT_PAGE_SIZE = 30;

    /**
     * The size of returned result page when no size is specified.
     *
     * @return
     */
    protected int getDefaultPageSize() {
        return DEFAULT_PAGE_SIZE;
    }

    @Override
    public Page<T> find(@NotNull Token token) throws DocumentException {
        return this.find(token, 0, this.getDefaultPageSize(), null);
    }

    @Override
    public Page<T> find(
            @NotNull Token token, int offset, int size)
            throws DocumentException {
        return this.find(token, offset, size, null);
    }

    @Logged
    @Override
    public Page<T> find(
            @NotNull Token token, int offset, int size, @Nullable Object query)
            throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            return this.find(
                session.toJcrSession(), offset, size, query);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Override
    public T find(@NotNull Token token, @NotNull  String path)
            throws DocumentException {
        try (UserSession session = getSession(token).open()) {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (path.equals("")) {
                path = ".";
            }
            return this.find(
                    session.toJcrSession(), path);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

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
    abstract protected Page<T> find(
            @NotNull Session session,
            int offset,
            int size,
            @Nullable Object query)
            throws RepositoryException, DocumentException;

    /**
     * Perform path look-up.
     *
     * @param session
     * @param path
     * @return
     * @throws RepositoryException
     * @throws DocumentException
     */
    abstract public T find(@NotNull Session session, String path)
            throws RepositoryException, DocumentException;
}
