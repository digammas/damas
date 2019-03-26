package solutions.digamma.damas.content;

import solutions.digamma.damas.entity.Entity;

import java.util.List;

/**
 * Object of this type can receive comments.
 *
 * @author Ahmad Shahwan
 */
public interface CommentReceiver extends Entity {

    /**
     * All received comments.
     * Can be empty, but not null.
     *
     * @return
     */
    List<Comment> getComments();
}
