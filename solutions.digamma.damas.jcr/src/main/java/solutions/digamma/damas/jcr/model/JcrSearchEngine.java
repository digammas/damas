package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.SearchEngine;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.error.JcrException;
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
     * The size of returned result page when no size is specified.
     *
     * @return
     */
    default int getDefaultPageSize() {
        return DEFAULT_PAGE_SIZE;
    }

    @Override
    default Page<T> find(@NotNull Token token) throws DocumentException {
        return this.find(token, 0, this.getDefaultPageSize(), null);
    }

    @Override
    default Page<T> find(
            @NotNull Token token, int offset, int size)
            throws DocumentException {
        return this.find(token, offset, size, null);
    }

    @Logged
    @Override
    default Page<T> find(
            @NotNull Token token, int offset, int size, @Nullable Object query)
            throws DocumentException {
        try (SessionWrapper session = openSession(token)) {
            return this.find(
                    session.getSession(), offset, size, query);
        } catch (RepositoryException e) {
            throw JcrException.of(e);
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
    Page<T> find(
            @NotNull Session session,
            int offset,
            int size,
            @Nullable Object query)
            throws RepositoryException, DocumentException;
}
