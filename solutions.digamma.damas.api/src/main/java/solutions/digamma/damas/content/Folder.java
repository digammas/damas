package solutions.digamma.damas.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;

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
    @NotNull DetailedFolder expand() throws DocumentException;

    /**
     * Expand content to the given depth.
     *
     * @param depth
     */
    void expandContent(long depth);

    /**
     * Expand content to the leaves.
     */
    void expandContent();

    /**
     * Retrieve all files in a folder, recursively to a the depth defined by
     * {@code expandContent()}. By default the depth is zero.
     *
     * This method returns {@code null} if content is hidden or collapsed.
     *
     * @return
     */
    @Nullable Content getContent();

    /**
     * Folder's content object.
     */
    interface Content {

        /**
         * Array of all sub-folders in a folder.
         *
         * @return
         */
        @NotNull Folder @NotNull [] getFolders();

        /**
         * Array of all documents in a folder.
         * @return
         */
        @NotNull Folder @NotNull [] getDocuments();
    }
}
