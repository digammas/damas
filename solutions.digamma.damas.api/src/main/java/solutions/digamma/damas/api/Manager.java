package solutions.digamma.damas.api;

import solutions.digamma.damas.api.inspection.Nonnull;
import solutions.digamma.damas.api.inspection.Nullable;

/**
 * Entity CRUD manager.
 *
 * @auther Ahmad Shahwan
 */
public interface Manager<T extends Entity> {

    /**
     * Create a new entity.
     *
     * @param entity                Entity to create.
     * @return                      Newly created entity.
     * @throws DocumentException
     */
    @Nonnull T create(@Nonnull  T entity)
            throws DocumentException;

    /**
     * Retrieve an existing entity.
     *
     * @param id                    ID of the entity to be retrieved.
     * @return                      Retrieved entity.
     * @throws DocumentException
     */
    @Nonnull T retrieve(@Nonnull String id)
            throws DocumentException;

    /**
     * Update and existing entity.
     *
     * @param id                    ID of entity to be updated.
     * @param entity                Pattern object, containing only values to be
     *                              updated.
     * @return                      Updated entity.
     * @throws DocumentException
     */
    @Nonnull T update(@Nonnull String id, @Nonnull T entity)
            throws DocumentException;

    /**
     * Delete an existing entity.
     *
     * @param id                    ID of the entity to be deleted.
     * @throws DocumentException
     */
    void delete(@Nonnull String id)
            throws DocumentException;

    /**
     * Find all entities.
     *
     * @return                      A page of all known entities.
     */
    @Nonnull Page<T> find();

    /**
     * Find entities from an offset, up to certain size.
     *
     * @param offset                Search offset.
     * @param size                  Maximum result size.
     * @return                      A page of entities.
     */
    @Nonnull Page<T> find(int offset, int size);

    /**
     * Find entities from an offset, up to certain size, satisfying a give
     * query.
     *
     * @param offset                Search offset.
     * @param size                  Maximum result size.
     * @param query                 Search query. If null, no filtering is done.
     * @return                      A page of entities.
     */
    @Nonnull Page<T> find(int offset, int size, @Nullable Object query);
}
