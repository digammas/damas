package solutions.digamma.damas.content;

import solutions.digamma.damas.entity.Created;
import solutions.digamma.damas.common.WorkspaceException;
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
     * @throws WorkspaceException
     */
    String getText() throws WorkspaceException;

    /**
     * Set comment text.
     *
     * @param value
     * @throws WorkspaceException
     */
    void setText(String value) throws WorkspaceException;

    /**
     * Get ID of the entity comment to witch this comment replies.
     *
     * @return
     * @throws WorkspaceException
     */
    String getReceiverId() throws WorkspaceException;

    /**
     * Get the entity to witch this comment replies if such a comment exist, or
     * null otherwise.
     *
     * @return
     * @throws WorkspaceException
     */
    CommentReceiver getReceiver() throws WorkspaceException;

    /**
     * Comment rank, can be negative.
     *
     * @return
     * @throws WorkspaceException
     */
    Long getRank() throws WorkspaceException;

    /**
     * Set comment rank;
     *
     * @param value
     * @throws WorkspaceException
     */
    void setRank(Long value) throws WorkspaceException;
}
