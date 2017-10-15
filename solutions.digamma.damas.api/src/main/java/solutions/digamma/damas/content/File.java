package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;

/**
 * File object. A generalization of a documents and folders.
 *
 * @author Ahmad Shahwan
 */
public interface File extends Entity {

    /**
     * A value returned by {@code getParent()} when the file is root.
     */
    Folder NO_PARENT = null;

    /**
     * A value returned by {@code getParentId()} when the file is root.
     */
    String NO_PARENT_ID = "";

    /**
     * File name.
     *
     * @return
     */
    @Nullable String getName() throws WorkspaceException;

    /**
     * Set file's name.
     *
     * @param value
     */
    void setName(@NotNull String value) throws WorkspaceException;

    /**
     * Parent folder.
     *
     * @return
     */
    @Nullable Folder getParent() throws WorkspaceException;

    /**
     * Set file's parent folder.
     *
     * @param value
     */
    void setParent(@NotNull Folder value) throws WorkspaceException;

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
     * Expand file into its detailed counterpart.
     *
     * @return
     * @throws WorkspaceException
     */
    @NotNull DetailedFile expand() throws WorkspaceException;

    /**
     * Update file with file information.
     *
     * @param other
     * @throws WorkspaceException
     */
    default void update(@NotNull File other) throws WorkspaceException {
        if (other.getName() != null) {
            this.setName(other.getName());
        }
        if (other.getParentId() != null) {
            this.setParentId(other.getParentId());
        }
    }
}
