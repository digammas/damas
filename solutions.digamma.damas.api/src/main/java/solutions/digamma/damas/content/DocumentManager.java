package solutions.digamma.damas.content;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.common.WorkspaceException;

import java.io.InputStream;

/**
 * Document manager.
 *
 * @author Ahmad Shahwan
 */
public interface DocumentManager
        extends CrudManager<Document>, PathFinder<Document> {

    /**
     * Create document with initial content.
     *
     * @param entity    document to be created
     * @param stream    document content
     * @return
     * @throws WorkspaceException
     */
    Document create(Document entity, InputStream stream)
            throws WorkspaceException;

    /**
     * Copy a document to a destination folder.
     *
     * @param sourceId          source document's ID
     * @param destinationId     destination folder's ID
     * @return                  new created document
     * @throws WorkspaceException
     */
    Document copy(String sourceId, String destinationId)
            throws WorkspaceException;

    /**
     * Read document's content.
     *
     * @param id    document's ID
     * @return      document's content
     */
    DocumentPayload download(String id)
            throws WorkspaceException;

    /**
     * Write a stream to a document, replacing existing content.
     *
     * @param id        document's ID
     * @param stream    document's new content
     */
    void upload(String id, InputStream stream) throws WorkspaceException;
}
