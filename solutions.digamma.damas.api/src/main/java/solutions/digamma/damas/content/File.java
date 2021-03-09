package solutions.digamma.damas.content;

import java.util.List;
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
    String getName();

    /**
     * Set file's name.
     *
     * @param value
     */
    void setName(String value);

    /**
     * Parent folder. When the current file is the root folder, this method
     * returns {@code null}.
     *
     * @return
     */
    Folder getParent();

    /**
     * Set file's parent folder.
     *
     * @param value
     */
    void setParent(Folder value);

    /**
     * Parent ID. When the current file is the root folder, this method returns
     * {@code null}.
     *
     * @return
     */
    String getParentId();

    /**
     * Set parent ID.
     *
     * @param value
     */
    void setParentId(String value);

    /**
     * File's metadata.
     *
     * @return
     */
    Metadata getMetadata();

    /**
     * Update file's metadata. If value is null, delete metadata. Only present
     * properties of metadata will be updated. Absent properties remain
     * unchanged.
     *
     * @param metadata
     */
    void updateMetadata(Metadata metadata);

    /**
     * Node's path.
     * A path is composed of node names, starting from the root node represented
     * as an empty string, down to the current node. Names are separated by
     * forward slashes.
     *
     * @return
     */
    String getPath();

    /**
     * Node's ancestors identifiers.
     *
     * Identifiers are ordered as per nodes appearance in path.
     *
     * @return
     */
    List<String> getPathIds();
}
