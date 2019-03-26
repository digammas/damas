package solutions.digamma.damas.content;

import java.util.List;

/**
 * Folder object. A folder can contain files and other folders.
 *
 * @author Ahmad Shahwan
 */
public interface Folder extends File {

    /**
     * Expand content to the given depth.
     *
     * @param depth
     */
    void expandContent(int depth);

    /**
     * Expand content down to the leaves.
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
    Content getContent();

    /**
     * Folder's content object.
     */
    interface Content {

        /**
         * Array of all sub-folders in a folder.
         *
         * @return
         */
        List<? extends Folder> getFolders();

        /**
         * Array of all documents in a documents.
         * @return
         */
        List<? extends Document> getDocuments();
    }
}
