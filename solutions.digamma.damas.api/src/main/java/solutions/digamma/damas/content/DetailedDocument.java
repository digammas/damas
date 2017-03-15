package solutions.digamma.damas.content;

import solutions.digamma.damas.inspection.Nonnull;

/**
 * Detailed document.
 *
 * @author Ahmad Shahwan
 */
public interface DetailedDocument extends Document, DetailedFile {

    /**
     * All versions of the document.
     * @return
     */
    @Nonnull Version @Nonnull[] getVersions();
}
