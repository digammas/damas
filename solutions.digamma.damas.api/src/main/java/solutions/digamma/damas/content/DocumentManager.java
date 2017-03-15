package solutions.digamma.damas.content;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.Nonnull;

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
    @Nonnull Document create(
            @Nonnull Token token,
            @Nonnull Document entity,
            @Nonnull InputStream stream)
            throws DocumentException;

    /**
     * Read document's content.
     *
     * @param token
     * @param id
     * @return
     */
    DocumentPayload download(@Nonnull Token token, @Nonnull String id)
            throws DocumentException;

    /**
     * Write a stream to a document, replacing existing content.
     *
     * @param token
     * @param id
     * @param stream
     */
    void upload(
            @Nonnull Token token,
            @Nonnull String id,
            @Nonnull InputStream stream)
            throws DocumentException;
}
