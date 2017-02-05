package solutions.digamma.damas.api.content;

import solutions.digamma.damas.api.Created;
import solutions.digamma.damas.api.Entity;
import solutions.digamma.damas.api.Modifiable;
import solutions.digamma.damas.api.inspection.Nullable;

/**
 * A comment that can be added to a comment receiver.
 *
 * @author Ahmad Shawan
 */
public interface Comment extends Entity, Created, Modifiable, CommentReceiver {

    /**
     * Comment text.
     *
     * @return
     */
    String getText();

    /**
     * Set comment text.
     *
     * @param value
     */
    void setText(@Nullable String value);

    /**
     * Get ID of the entity comment to witch this comment replies.
     *
     * @return
     */
    @Nullable String getReceiverId();

    /**
     * Get the entity to witch this comment replies if such a comment exist, or
     * null otherwise.
     *
     * @return
     */
    @Nullable CommentReceiver getReceiver();

    /**
     * Comment rank, can be negative.
     *
     * @return
     */
    @Nullable Integer getRank();

    /**
     * Set comment rank;
     *
     * @param value
     */
    void setRank(@Nullable Integer value);
}
