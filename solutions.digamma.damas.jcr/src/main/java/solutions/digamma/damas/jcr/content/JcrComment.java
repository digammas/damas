package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.InternalStateException;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.CommentReceiver;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.Namespace;
import solutions.digamma.damas.jcr.error.JcrException;
import solutions.digamma.damas.jcr.model.JcrBaseEntity;
import solutions.digamma.damas.jcr.model.JcrCreated;
import solutions.digamma.damas.jcr.model.JcrModifiable;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

/**
 * @author Ahmad Shahwan
 */
public class JcrComment extends JcrBaseEntity
        implements Comment, JcrCommentReceiver, JcrCreated, JcrModifiable {

    /**
     * No argument constructor.
     *
     * @param node  comment's JCR node
     * @throws WorkspaceException
     */
    JcrComment(@NotNull Node node) throws WorkspaceException {
        super(node);
    }

    @Override
    public String getText() throws WorkspaceException {
        return this.getString(Property.JCR_CONTENT);
    }

    @Override
    public void setText(@Nullable String value) throws WorkspaceException {
        this.setString(Property.JCR_CONTENT, value);
    }

    @Override
    public String getReceiverId()throws WorkspaceException {
        try {
            return this.getNode().getParent().getIdentifier();
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    @Override
    public CommentReceiver getReceiver() throws WorkspaceException {
        return (JcrCommentReceiver) () -> JcrComment.this.node;
    }

    @Override
    public Long getRank()throws WorkspaceException {
        return this.getLong(Namespace.RANK);
    }

    @Override
    public void setRank(@Nullable Long value) throws WorkspaceException {
        this.setLong(Namespace.RANK, value);
    }

    @Override
    protected void checkCompatibility() throws InternalStateException {
        this.checkTypeCompatibility(Namespace.COMMENT);
    }

    public void update(@NotNull Comment other) throws WorkspaceException {
        if (other.getRank() != null) {
            this.setRank(other.getRank());
        }
        if (other.getText() != null) {
            this.setText(other.getText());
        }
    }
}
