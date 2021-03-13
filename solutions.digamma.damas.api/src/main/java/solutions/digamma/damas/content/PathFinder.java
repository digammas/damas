package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;

/**
 * Path look-up enabler.
 *
 * This interface provides a functionality that allows finding an entity given
 * its path.
 *
 * @author Ahmad Shahwan
 */
public interface PathFinder<T extends Entity> {

    /**
     * Find the entity located at {@code path}.
     *
     * @param path      Path of the looked up entity.
     * @return          The entity whose path is {@code path}.
     * @throws WorkspaceException    If not such path exists, or other errors.
     */
    T find(String path) throws WorkspaceException;
}
