package solutions.digamma.damas;

import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;

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
     * @throws DocumentException
     */
    @NotNull
    T retrieve(@NotNull Token token, @NotNull String id)
            throws DocumentException;

}
