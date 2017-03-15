package solutions.digamma.damas.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.inspection.Nonnull;

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
     * @throws DocumentException
     */
    @Nonnull Comment[] getComments() throws DocumentException;
}
