package solutions.digamma.damas.content;

import solutions.digamma.damas.Created;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.Modifiable;
import solutions.digamma.damas.inspection.Nullable;

/**
 * A comment that can be added to a comment receiver.
 * A comment is a comment receiver itself.
 *
 * @author Ahmad Shawan
 */
public interface Comment extends Entity, Created, Modifiable, CommentReceiver {

    /**
     * Comment text.
     *
     * @return
     * @throws DocumentException
     */
    String getText() throws DocumentException;

    /**
     * Set comment text.
     *
     * @param value
     * @throws DocumentException
     */
    void setText(@Nullable String value) throws DocumentException;

    /**
     * Get ID of the entity comment to witch this comment replies.
     *
     * @return
     * @throws DocumentException
     */
    @Nullable String getReceiverId() throws DocumentException;

    /**
     * Get the entity to witch this comment replies if such a comment exist, or
     * null otherwise.
     *
     * @return
     * @throws DocumentException
     */
    @Nullable CommentReceiver getReceiver() throws DocumentException;

    /**
     * Comment rank, can be negative.
     *
     * @return
     * @throws DocumentException
     */
    @Nullable Long getRank() throws DocumentException;

    /**
     * Set comment rank;
     *
     * @param value
     * @throws DocumentException
     */
    void setRank(@Nullable Long value) throws DocumentException;
}
