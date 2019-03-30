package solutions.digamma.damas.entity;

import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.entity.Page;
import solutions.digamma.damas.login.Token;
import solutions.digamma.damas.common.WorkspaceException;

/**
 * Entity search facilities manager.
 *
 * @author Ahmad Shahwan
 */
public interface SearchEngine<T extends Entity> {

    /**
     * Find all entities.
     *
     * @return A page of all known entities.
     */
    Page<T> find() throws WorkspaceException;

    /**
     * Find entities starting from a given offset, up to certain size.
     *
     * @param offset Search offset.
     * @param size   Maximum result size.
     * @return A page of entities.
     */
    Page<T> find(int offset, int size)
            throws WorkspaceException;

    /**
     * Find entities satisfying a query starting from a given offset, and up to
     * certain size.
     *
     * @param offset Search offset.
     * @param size   Maximum result size.
     * @param query  Search query. If null, no filtering is done.
     * @return A page of entities.
     */
    Page<T> find(int offset, int size, Object query)
            throws WorkspaceException;
}
