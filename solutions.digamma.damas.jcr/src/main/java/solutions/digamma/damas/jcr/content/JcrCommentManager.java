package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.CommentManager;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.model.JcrCrudManager;
import solutions.digamma.damas.jcr.model.JcrSearchEngine;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * JCR-based comment manager implementation.
 *
 * @author Ahmad Shahwan
 */
public class JcrCommentManager
        extends JcrCrudManager<Comment>
        implements JcrSearchEngine<Comment>, CommentManager {

    @Override
    protected Comment retrieve(Session session, String id) throws RepositoryException, DocumentException {
        return null;
    }

    @Override
    protected Comment create(Session session, @NotNull Comment entity) throws RepositoryException, DocumentException {
        return null;
    }

    @Override
    protected Comment update(Session session, String id, @NotNull Comment entity) throws RepositoryException, DocumentException {
        return null;
    }

    @Override
    protected void delete(Session session, String id) throws RepositoryException, DocumentException {

    }

    @Override
    public Page<Comment> find(Session session, int offset, int size, Object query) throws RepositoryException, DocumentException {
        return null;
    }
}
