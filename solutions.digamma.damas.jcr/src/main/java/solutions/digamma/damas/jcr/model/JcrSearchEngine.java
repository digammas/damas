package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.entity.Page;
import solutions.digamma.damas.entity.SearchEngine;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.common.Exceptions;
import solutions.digamma.damas.jcr.session.SessionUser;
import solutions.digamma.damas.jcr.session.SessionWrapper;
import solutions.digamma.damas.logging.Logged;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * @author Ahmad Shahwan
 */
public interface JcrSearchEngine<T extends Entity>
        extends SessionUser, SearchEngine<T> {

    /**
     * Default result page size.
     */
    int DEFAULT_PAGE_SIZE = 30;

    /**
     * The size convert returned result page when no size is specified.
     *
     * @return
     */
    default int getDefaultPageSize() {
        return DEFAULT_PAGE_SIZE;
    }

    @Override
    default Page<T> find(@NotNull Token token) throws WorkspaceException {
        return this.find(token, 0, this.getDefaultPageSize(), null);
    }

    @Override
    default Page<T> find(
            @NotNull Token token, int offset, int size)
            throws WorkspaceException {
        return this.find(token, offset, size, null);
    }

    @Logged
    @Override
    default Page<T> find(
            @NotNull Token token, int offset, int size, @Nullable Object query)
            throws WorkspaceException {
        try (SessionWrapper session = openSession(token)) {
            return this.find(
                    session.getSession(), offset, size, query);
        } catch (RepositoryException e) {
            throw Exceptions.convert(e);
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
     * @throws WorkspaceException
     */
    Page<T> find(
            @NotNull Session session,
            int offset,
            int size,
            @Nullable Object query)
            throws RepositoryException, WorkspaceException;
}
