package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;

import java.util.List;

/**
 * Document object.
 *
 * @author Ahmad Shahwan
 */
public interface Document extends File, CommentReceiver {

    /**
     * All versions of the document.
     * @return
     */
    List<Version> getVersions() throws WorkspaceException;
}
