package solutions.digamma.damas.api.content;

import solutions.digamma.damas.api.Entity;
import solutions.digamma.damas.api.inspection.Nonnull;

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
    @Nonnull
    Comment[] getComments();
}
