package solutions.digamma.damas.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
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
    String NO_PARENT_ID = new String();

    /**
     * File name.
     *
     * @return
     */
    @Nullable String getName() throws DocumentException;

    /**
     * Set file's name.
     *
     * @param value
     */
    void setName(@NotNull String value) throws DocumentException;

    /**
     * Parent folder.
     *
     * @return
     */
    @Nullable Folder getParent() throws DocumentException;

    /**
     * Set file's parent folder.
     *
     * @param value
     */
    void setParent(@NotNull Folder value) throws DocumentException;

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
     * Expand file into its detailed counterpart.
     *
     * @return
     * @throws DocumentException
     */
    @NotNull DetailedFile expand() throws DocumentException;

    /**
     * Update file with file information.
     *
     * @param other
     * @throws DocumentException
     */
    default void update(@NotNull File other) throws DocumentException {
        if (other.getName() != null) {
            this.setName(other.getName());
        }
        if (other.getParentId() != null) {
            this.setParentId(other.getParentId());
        }
    }
}
