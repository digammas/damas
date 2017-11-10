package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;

/**
 * Document object.
 *
 * @author Ahmad Shahwan
 */
public interface Document extends File {

    /**
     * Expand document to a detailed document.
     *
     * @return
     */
    @Override
    DetailedDocument expand() throws WorkspaceException;
}
