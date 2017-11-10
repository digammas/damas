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
     * @param token
     * @param entity
     * @param stream
     * @return
     * @throws WorkspaceException
     */
    Document create(
            Token token,
            Document entity,
            InputStream stream)
            throws WorkspaceException;

    /**
     * Read document's content.
     *
     * @param token
     * @param id
     * @return
     */
    DocumentPayload download(Token token, String id)
            throws WorkspaceException;

    /**
     * Write a stream to a document, replacing existing content.
     *
     * @param token
     * @param id
     * @param stream
     */
    void upload(
            Token token,
            String id,
            InputStream stream)
            throws WorkspaceException;
}
