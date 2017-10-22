package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.inspection.NotNull;

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
     * @throws WorkspaceException
     */
    @NotNull List<Comment> getComments() throws WorkspaceException;
}
