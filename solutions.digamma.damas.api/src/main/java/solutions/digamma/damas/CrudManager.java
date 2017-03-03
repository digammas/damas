package solutions.digamma.damas;

import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.Nonnull;

/**
 * CRUD entity manager. This is a manager that allow full CRUD (create,
 * retrieve, update and delete) to an entity.
 *
 * @author Ahmad Shahwan
 */
public interface CrudManager<T extends Entity> extends ReadManager<T> {

    /**
     * Create a new entity.
     *
     *
     * @param token
     * @param entity                Entity to create.
     * @return                      Newly created entity.
     * @throws DocumentException
     */
    @Nonnull
    T create(@Nonnull Token token, @Nonnull T entity)
            throws DocumentException;

    /**
     * Update and existing entity.
     *
     * @param id                    ID of entity to be updated.
     * @param token
     *@param entity                Pattern object, containing only values to be
     *                              updated.  @return                      Updated entity.
     * @throws DocumentException
     */
    @Nonnull T update(@Nonnull Token token, @Nonnull String id, @Nonnull T entity)
            throws DocumentException;

    /**
     * Delete an existing entity.
     *
     *
     * @param token
     * @param id                    ID of the entity to be deleted.
     * @throws DocumentException
     */
    void delete(@Nonnull Token token, @Nonnull String id)
            throws DocumentException;
}
