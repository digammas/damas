package solutions.digamma.damas;

import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;

/**
 * Entity search facilities manager.
 *
 * @author Ahmad Shahwan
 */
public interface SearchManager<T extends Entity> {

    /**
     * Find all entities.
     *
     * @param token
     * @return A page of all known entities.
     */
    @NotNull
    Page<T> find(@NotNull Token token) throws DocumentException;

    /**
     * Find entities from an offset, up to certain size.
     *
     * @param token
     * @param offset Search offset.
     * @param size   Maximum result size.
     * @return A page of entities.
     */
    @NotNull
    Page<T> find(@NotNull Token token, int offset, int size)
            throws DocumentException;

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
    @NotNull
    Page<T> find(
            @NotNull Token token, int offset, int size, @Nullable Object query)
            throws DocumentException;
}
