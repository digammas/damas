package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.CommentManager;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.Namespace;
import solutions.digamma.damas.jcr.error.IncompatibleNodeTypeException;
import solutions.digamma.damas.jcr.model.JcrCrudManager;
import solutions.digamma.damas.jcr.model.JcrSearchEngine;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.ConstraintViolationException;
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
            throws RepositoryException, DocumentException {
        return new JcrComment(session.getNodeByIdentifier(id));
    }

    @Override
    protected JcrComment create(Session session, @NotNull Comment entity)
            throws RepositoryException, DocumentException {
        String name = UUID.randomUUID().toString();
        try {
            Node parent = session.getNodeByIdentifier(entity.getReceiverId());
            Node node = parent.addNode(name, Namespace.COMMENT);
            JcrComment comment = new JcrComment(node);
            comment.update(entity);
            return comment;
        } catch (ConstraintViolationException e) {
            throw new IncompatibleNodeTypeException(
                    "Parent cannot receive comments.");
        } catch (ItemNotFoundException e) {
            throw new NotFoundException("Invalid parent ID");
        }
    }

    @Override
    protected JcrComment update(
            Session session, String id,
            @NotNull Comment entity)
            throws RepositoryException, DocumentException {
        JcrComment comment = this.retrieve(session, id);
        comment.update(entity);
        return comment;
    }

    @Override
    protected void delete(Session session, String id) throws RepositoryException, DocumentException {
        JcrComment comment = this.retrieve(session, id);
        comment.remove();
    }

    @Override
    public Page<Comment> find(
            Session session,
            int offset,
            int size,
            Object query)
            throws RepositoryException, DocumentException {
        return null;
    }
}
