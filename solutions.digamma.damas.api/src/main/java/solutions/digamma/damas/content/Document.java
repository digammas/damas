package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.inspection.NotNull;

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
    @NotNull DetailedDocument expand() throws WorkspaceException;
}
