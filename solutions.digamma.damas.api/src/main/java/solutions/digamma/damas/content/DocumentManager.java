package solutions.digamma.damas.content;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;

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
    @NotNull Document create(
            @NotNull Token token,
            @NotNull Document entity,
            @NotNull InputStream stream)
            throws WorkspaceException;

    /**
     * Read document's content.
     *
     * @param token
     * @param id
     * @return
     */
    DocumentPayload download(@NotNull Token token, @NotNull String id)
            throws WorkspaceException;

    /**
     * Write a stream to a document, replacing existing content.
     *
     * @param token
     * @param id
     * @param stream
     */
    void upload(
            @NotNull Token token,
            @NotNull String id,
            @NotNull InputStream stream)
            throws WorkspaceException;
}
