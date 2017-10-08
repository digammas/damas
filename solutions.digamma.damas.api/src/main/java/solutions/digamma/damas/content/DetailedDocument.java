package solutions.digamma.damas.content;

import solutions.digamma.damas.inspection.NotNull;

import java.util.List;

/**
 * Detailed document.
 *
 * @author Ahmad Shahwan
 */
public interface DetailedDocument
        extends Document, CommentReceiver, DetailedFile {

    /**
     * All versions of the document.
     * @return
     */
    @NotNull List<@NotNull Version> getVersions();
}
