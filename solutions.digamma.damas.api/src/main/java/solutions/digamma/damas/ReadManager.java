package solutions.digamma.damas;

import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.Nonnull;

/**
 * Read entity manager.
 * Basic manager that provides, if not coupled with other interfaces, a readonly
 * access.
 *
 * @author Ahmad Shahwan
 */
public interface ReadManager<T extends Entity> {

    /**
     * Retrieve an existing entity.
     *
     * @param token
     * @param id                    ID of the entity to be retrieved.
     * @return                      Retrieved entity.
     * @throws DocumentException
     */
    @Nonnull
    T retrieve(@Nonnull Token token, @Nonnull String id)
            throws DocumentException;

}
