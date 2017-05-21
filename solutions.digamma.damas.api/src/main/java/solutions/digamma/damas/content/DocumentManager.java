package solutions.digamma.damas.content;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;

import java.io.InputStream;

/**
 * Document manager.
 *
 * @author Ahmad Shahwan
 */
public interface DocumentManager extends CrudManager<Document> {

    /**
     * Create document with initial content.
     *
     * @param token
     * @param entity
     * @param stream
     * @return
     * @throws DocumentException
     */
    @NotNull Document create(
            @NotNull Token token,
            @NotNull Document entity,
            @NotNull InputStream stream)
            throws DocumentException;

    /**
     * Read document's content.
     *
     * @param token
     * @param id
     * @return
     */
    DocumentPayload download(@NotNull Token token, @NotNull String id)
            throws DocumentException;

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
            throws DocumentException;
}
