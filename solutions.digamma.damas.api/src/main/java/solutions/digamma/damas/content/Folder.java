package solutions.digamma.damas.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.inspection.Nullable;

/**
 * Folder object. A folder can contain files and other folders.
 *
 * @author Ahmad Shahwan
 */
public interface Folder extends File {

    /**
     * When passed to {@code showContent()} content is expanded to full depth.
     */
    long FULL_DEPTH = Long.MAX_VALUE;

    /**
     * Expand folder to a detailed document.
     *
     * @return
     */
    @Override
    @Nonnull DetailedFolder expand() throws DocumentException;

    void showContent(long depth);

    /**
     * Retrieve all documents in folder.
     * This method returns {@code null} if content is hidden or collapsed.
     *
     * @return
     */
    default @Nullable Document @Nonnull[] getDocuments() {
        return null;
    }

    /**
     * Retrieve all sub-folders, recursively to a certain depth.
     * This method returns {@code null} if content is hidden or collapsed.
     *
     * @return
     */
    default Folder @Nonnull [] getFolders() {
        return null;
    }
}
