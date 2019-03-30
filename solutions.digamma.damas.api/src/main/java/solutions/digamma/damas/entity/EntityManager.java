package solutions.digamma.damas.entity;

import solutions.digamma.damas.login.Token;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.common.WorkspaceException;

/**
 * Entity manager.
 * Basic manager that, unless coupled with other interfaces, provides a readonly
 * access.
 *
 * @author Ahmad Shahwan
 */
public interface EntityManager<T extends Entity> {

    /**
     * Retrieve an existing entity.
     *
     * @param token
     * @param id                    ID of the entity to be retrieved.
     * @return                      Retrieved entity.
     * @throws NotFoundException    When no such an entity exists.
     * @throws WorkspaceException
     */
    T retrieve(String id)
            throws WorkspaceException;

}
