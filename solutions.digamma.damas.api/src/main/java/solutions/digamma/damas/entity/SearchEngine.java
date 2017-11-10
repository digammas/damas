package solutions.digamma.damas.entity;

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
     * @param token
     * @return A page of all known entities.
     */
    Page<T> find(Token token) throws WorkspaceException;

    /**
     * Find entities from an offset, up to certain size.
     *
     * @param token
     * @param offset Search offset.
     * @param size   Maximum result size.
     * @return A page of entities.
     */
    Page<T> find(Token token, int offset, int size)
            throws WorkspaceException;

    /**
     * Find entities from an offset, up to certain size, satisfying a give
     * query.
     *
     * @param token
     * @param offset Search offset.
     * @param size   Maximum result size.
     * @param query  Search query. If null, no filtering is done.
     * @return A page of entities.
     */
    Page<T> find(
            Token token, int offset, int size, Object query)
            throws WorkspaceException;
}
