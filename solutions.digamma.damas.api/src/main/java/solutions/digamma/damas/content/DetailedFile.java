package solutions.digamma.damas.content;

import solutions.digamma.damas.entity.Created;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Modifiable;

/**
 * @author Ahmad Shahwan
 */
public interface DetailedFile
        extends File, Created, Modifiable {

    /**
     * File's metadata.
     *
     * @return
     * @throws WorkspaceException
     */
    Metadata getMetadata() throws WorkspaceException;

    /**
     * Update file's metadata. If value is null, delete metadata. Only present
     * properties of metadata will be updated. Absent properties remain
     * unchanged.
     *
     * @param metadata
     * @throws WorkspaceException
     */
    void setMetadata(Metadata metadata) throws WorkspaceException;

    /**
     * Parent path.
     * A path is composed of node names, starting from the root node represented
     * as an empty string, down to the current node. Names are separated by
     * forward slashes.
     *
     * @return
     */
    String getPath() throws WorkspaceException;
}
