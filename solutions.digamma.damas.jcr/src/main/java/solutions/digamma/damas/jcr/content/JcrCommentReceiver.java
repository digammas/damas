package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.CommentReceiver;
import solutions.digamma.damas.jcr.model.JcrEntity;
import solutions.digamma.damas.jcr.fail.JcrException;
import solutions.digamma.damas.jcr.Namespace;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.ArrayList;
import java.util.List;

/**
 * JCR comment receiver.
 *
 * @author Ahmad Shahwan
 */
public interface JcrCommentReceiver extends CommentReceiver, JcrEntity {

    @Nonnull
    default Comment[] getComments() throws DocumentException {
        try {
            String sql2 = String.format(
                    SQL2_SELECT_CHILDREN,
                    Namespace.COMMENT,
                    this.getNode().getPath());
            QueryManager manager = this.getNode()
                    .getSession().getWorkspace().getQueryManager();
            Query query = manager.createQuery(sql2, Query.JCR_SQL2);
            QueryResult result = query.execute();
            NodeIterator nodes = result.getNodes();
            List<Comment> comments = new ArrayList<>();
            while (nodes.hasNext()) {
                comments.add(new JcrComment(nodes.nextNode()));
            }
            return comments.toArray(new Comment[0]);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }
}
