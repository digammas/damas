package solutions.digamma.damas.content;

import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.inspection.NotNull;

/**
 * Path look-up enabler.
 *
 * @author Ahmad Shahwan
 */
public interface PathFinder<T extends Entity> {

    /**
     * Find the entity located at {@code path}.
     *
     * @param token     Access token.
     * @param path      Path of the looked up entity.
     * @return          The entity whose path is {@code path}.
     * @throws WorkspaceException    If not such path exists, or other errors.
     */
    @NotNull T find(@NotNull Token token, @NotNull String path)
            throws WorkspaceException;
}
