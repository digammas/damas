package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;

import java.io.InputStream;

/**
 * Document's binary content.
 *
 * @author Ahmad Shahwan
 */
public interface DocumentPayload {

    /**
     * Content size in bytes.
     *
     * @return
     */
    long getSize() throws WorkspaceException;

    /**
     * Binary content as stream.
     *
     * @return
     */
    InputStream getStream() throws WorkspaceException;
}
