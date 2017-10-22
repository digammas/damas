package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.CommentReceiver;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.common.Exceptions;
import solutions.digamma.damas.jcr.model.JcrEntity;
import solutions.digamma.damas.jcr.names.TypeNamespace;

import javax.jcr.NodeIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JCR comment receiver.
 *
 * @author Ahmad Shahwan
 */
public interface JcrCommentReceiver extends CommentReceiver, JcrEntity {

    @NotNull
    default List<Comment> getComments() throws WorkspaceException {
        List<Comment> comments = new ArrayList<>();
        Exceptions.wrap(() -> {
            NodeIterator nodes = this.getChildNodes(TypeNamespace.COMMENT);
            while (nodes.hasNext()) {
                comments.add(new JcrComment(nodes.nextNode()));
            }
        });
        return Collections.unmodifiableList(comments);
    }
}
