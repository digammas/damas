package solutions.digamma.damas.content;

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
    long getSize();

    /**
     * Binary content as stream.
     *
     * @return
     */
    InputStream getStream();
}
