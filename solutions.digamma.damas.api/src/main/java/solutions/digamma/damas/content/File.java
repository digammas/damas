package solutions.digamma.damas.content;

import solutions.digamma.damas.Created;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.Modifiable;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.inspection.Nullable;

/**
 * File object. A generalization of a documents and folders.
 *
 * @author Ahmad Shahwan
 */
public interface File extends Entity, CommentReceiver, Created, Modifiable {

    /**
     * A value returned by {@code getParent()} when the file is root.
     */
    Folder NO_PARENT = null;

    /**
     * A value returned by {@code getParentId()} when the file is root.
     */
    String NO_PARENT_ID = new String();

    /**
     * File name.
     *
     * @return
     */
    @Nullable
    String getName() throws DocumentException;

    /**
     * Set file's name.
     *
     * @param value
     */
    void setName(@Nonnull String value) throws DocumentException;

    /**
     * Parent folder.
     *
     * @return
     */
    @Nullable
    Folder getParent() throws DocumentException;

    /**
     * Set file's parent folder.
     *
     * @param value
     */
    void setParentId(@Nonnull Folder value) throws DocumentException;

    /**
     * Parent ID.
     *
     * @return
     */
    String getParentId() throws DocumentException;

    /**
     * Set parent ID.
     *
     * @param value
     */
    void setParentId(String value) throws DocumentException;

    /**
     * Parent path.
     * A path is composed of node names, starting from the root node represented
     * as an empty string, down to the current node. Names are separated by
     * forward slashes.
     *
     * @return
     */
    @Nullable
    String getPath() throws DocumentException;

    /**
     * File's metadata.
     *
     * @return
     * @throws DocumentException
     */
    @Nullable
    Metadata getMetadata() throws DocumentException;

    /**
     * Update file's metadata. If value is null, delete metadata. Only present
     * properties of metadata will be updated. Absent properties remain
     * unchanged.
     *
     * @param metadata
     * @throws DocumentException
     */
    void setMetadata(@Nullable Metadata metadata) throws DocumentException;

    /**
     * Update file with file information.
     *
     * @param other
     * @throws DocumentException
     */
    default void update(@Nonnull File other) throws DocumentException {
        if (other.getName() != null) {
            this.setName(other.getName());
        }
        if (other.getParentId() != null) {
            this.setParentId(other.getParentId());
        }
        if (other.getMetadata() != null) {
            this.setMetadata(other.getMetadata());
        }
    }

    /**
     * Whether file is root.
     *
     * @return
     * @throws DocumentException
     */
    default boolean isRoot() throws DocumentException {
        return getParentId() == NO_PARENT_ID;
    }
}
