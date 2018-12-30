package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Created;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.entity.Modifiable;

/**
 * File object. A generalization of a documents and folders.
 *
 * @author Ahmad Shahwan
 */
public interface File extends Entity, Created, Modifiable {

    /**
     * A value returned by {@code getParentId()} when the file is root.
     */
    String NO_PARENT_ID = "";

    /**
     * File name.
     *
     * @return
     */
    String getName() throws WorkspaceException;

    /**
     * Set file's name.
     *
     * @param value
     */
    void setName(String value) throws WorkspaceException;

    /**
     * Parent folder. When the current file is the root folder, this method
     * return {@code null}.
     *
     * @return
     */
    Folder getParent() throws WorkspaceException;

    /**
     * Set file's parent folder.
     *
     * @param value
     */
    void setParent(Folder value) throws WorkspaceException;

    /**
     * Parent ID.
     *
     * @return
     */
    String getParentId() throws WorkspaceException;

    /**
     * Set parent ID.
     *
     * @param value
     */
    void setParentId(String value) throws WorkspaceException;

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
