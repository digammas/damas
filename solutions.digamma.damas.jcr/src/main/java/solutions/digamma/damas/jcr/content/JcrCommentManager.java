package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.common.CompatibilityException;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Page;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.CommentManager;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.names.TypeNamespace;
import solutions.digamma.damas.jcr.model.JcrCrudManager;
import solutions.digamma.damas.jcr.model.JcrSearchEngine;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.UUID;

/**
 * JCR-based comment manager implementation.
 *
 * @author Ahmad Shahwan
 */
public class JcrCommentManager
        extends JcrCrudManager<Comment>
        implements JcrSearchEngine<Comment>, CommentManager {

    @Override
    protected JcrComment retrieve(Session session, String id)
            throws RepositoryException, WorkspaceException {
        return new JcrComment(session.getNodeByIdentifier(id));
    }

    @Override
    protected JcrComment create(Session session, @NotNull Comment entity)
            throws RepositoryException, WorkspaceException {
        String name = UUID.randomUUID().toString();
        Node parent = session.getNodeByIdentifier(entity.getReceiverId());
        if (!parent.isNodeType(TypeNamespace.COMMENT_RECEIVER)) {
            throw new CompatibilityException(
                    "Parent cannot receive comments.");
        }
        Node node = parent.addNode(name, TypeNamespace.COMMENT);
        JcrComment comment = new JcrComment(node);
        comment.update(entity);
        return comment;
    }

    @Override
    protected JcrComment update(
            Session session, String id,
            @NotNull Comment entity)
            throws RepositoryException, WorkspaceException {
        JcrComment comment = this.retrieve(session, id);
        comment.update(entity);
        return comment;
    }

    @Override
    protected void delete(Session session, String id)
            throws RepositoryException, WorkspaceException {
        JcrComment comment = this.retrieve(session, id);
        comment.remove();
    }

    @Override
    public Page<Comment> find(
            Session session,
            int offset,
            int size,
            Object query)
            throws RepositoryException, WorkspaceException {
        return null;
    }
}
