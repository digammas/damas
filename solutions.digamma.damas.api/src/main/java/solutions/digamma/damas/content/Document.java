package solutions.digamma.damas.content;

import java.util.List;

/**
 * Document object.
 *
 * @author Ahmad Shahwan
 */
public interface Document extends File, CommentReceiver {

    String getMimeType();

    void setMimeType(String value);

    /**
     * All versions of the document.
     * @return
     */
    List<Version> getVersions();
}
