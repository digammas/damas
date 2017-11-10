package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;

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
    List<Version> getVersions() throws WorkspaceException;
}
