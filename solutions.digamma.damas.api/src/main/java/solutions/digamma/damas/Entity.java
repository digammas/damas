package solutions.digamma.damas;

import solutions.digamma.damas.inspection.Nullable;

/**
 * Generic persistent entity.
 *
 * When an entity is used as an updater, unchanged properties remain null. Only
 * properties to be updated return a non-null value.
 *
 * Some properties can be unchangeable.
 *
 * @author Ahmad Shahwan
 */
public interface Entity {

    /**
     * Entity identifier.
     *
     * Can be null when the entity is new created and not yet persisted.
     *
     * @return
     */
    @Nullable
    String getId() throws WorkspaceException;
}