package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;

import java.util.List;

/**
 * Folder object. A folder can contain files and other folders.
 *
 * @author Ahmad Shahwan
 */
public interface Folder extends File {

    /**
     * Expand folder to a detailed document.
     *
     * @return
     */
    @Override
    @NotNull DetailedFolder expand() throws WorkspaceException;

    /**
     * Expand content to the given depth.
     *
     * @param depth
     */
    void expandContent(int depth) throws WorkspaceException;

    /**
     * Expand content down to the leaves.
     */
    void expandContent() throws WorkspaceException;

    /**
     * Retrieve all files in a folder, recursively to a the depth defined by
     * {@code expandContent()}. By default the depth is zero.
     *
     * This method returns {@code null} if content is hidden or collapsed.
     *
     * @return
     * @throws WorkspaceException
     */
    @Nullable Content getContent() throws WorkspaceException;

    /**
     * Folder's content object.
     */
    interface Content {

        /**
         * Array of all sub-folders in a folder.
         *
         * @return
         */
        @NotNull List<@NotNull ? extends Folder> getFolders();

        /**
         * Array of all documents in a documents.
         * @return
         */
        @NotNull List<@NotNull ? extends Document> getDocuments();
    }
}
