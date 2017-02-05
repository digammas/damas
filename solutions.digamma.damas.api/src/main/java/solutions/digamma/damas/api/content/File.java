package solutions.digamma.damas.api.content;

import solutions.digamma.damas.api.DocumentException;
import solutions.digamma.damas.api.Entity;
import solutions.digamma.damas.api.inspection.Nonnull;
import solutions.digamma.damas.api.inspection.Nullable;

/**
 * File object. A generalization of a documents and folders.
 *
 * @author Ahmad Shahwan
 */
public interface File extends Entity, CommentReceiver {

    /**
     * A value returned by {@code getParent()} when the file is root.
     */
    static final Folder NO_PARENT = null;

    /**
     * A value returned by {@code getParentId()} when the file is root.
     */
    static final String NO_PARENT_ID = null;

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
     * Whether file is root.
     *
     * @return
     * @throws DocumentException
     */
    default boolean isRoot() throws DocumentException {
        return getParentId() == NO_PARENT_ID;
    }
}
