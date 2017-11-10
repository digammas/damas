package solutions.digamma.damas.entity;

import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.common.WorkspaceException;

/**
 * CRUD entity manager. This is a manager that allow full CRUD (create,
 * retrieve, update and delete) to an entity.
 *
 * @author Ahmad Shahwan
 */
public interface CrudManager<T extends Entity> extends EntityManager<T> {

    /**
     * Create a new entity.
     *
     *
     * @param token
     * @param entity                Entity to create.
     * @return                      Newly created entity.
     * @throws WorkspaceException
     */
    T create(Token token, T entity)
            throws WorkspaceException;

    /**
     * Update and existing entity.
     *
     * @param id                    ID of entity to be updated.
     * @param token
     * @param entity                Pattern object, containing only values to be
     *                              updated.
     * @return                      Updated entity.
     * @throws WorkspaceException
     */
    T update(
            Token token,
            String id,
            T entity)
            throws WorkspaceException;

    /**
     * Delete an existing entity.
     *
     *
     * @param token
     * @param id                    ID of the entity to be deleted.
     * @throws WorkspaceException
     */
    void delete(Token token, String id)
            throws WorkspaceException;
}
