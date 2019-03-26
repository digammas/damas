package solutions.digamma.damas.content;

import solutions.digamma.damas.entity.Created;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.entity.Modifiable;

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
     */
    String getText();

    /**
     * Set comment text.
     *
     * @param value
     */
    void setText(String value);

    /**
     * Get ID of the entity comment to witch this comment replies.
     *
     * @return
     */
    String getReceiverId();

    /**
     * Get the entity to witch this comment replies if such a comment exist, or
     * null otherwise.
     *
     * @return
     */
    CommentReceiver getReceiver();

    /**
     * Comment rank, can be negative.
     *
     * @return
     */
    Long getRank();

    /**
     * Set comment rank;
     *
     * @param value
     */
    void setRank(Long value);
}
