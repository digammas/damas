package solutions.digamma.damas.content;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.login.Token;

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
     * @param entity
     * @param stream
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
     * @param id
     * @return
     */
    DocumentPayload download(String id)
            throws WorkspaceException;

    /**
     * Write a stream to a document, replacing existing content.
     *
     * @param id
     * @param stream
     */
    void upload(String id, InputStream stream) throws WorkspaceException;
}
