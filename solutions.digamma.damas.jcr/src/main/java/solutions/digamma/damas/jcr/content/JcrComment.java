package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.CommentReceiver;
import solutions.digamma.damas.jcr.model.JcrCreated;
import solutions.digamma.damas.jcr.model.JcrModifiable;
import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.jcr.model.JcrBaseEntity;
import solutions.digamma.damas.jcr.error.JcrExceptionMapper;
import solutions.digamma.damas.jcr.Namespace;

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
     * @throws DocumentException
     */
    JcrComment(@NotNull Node node) throws DocumentException {
        super(node);
    }

    @Override
    public String getText() throws DocumentException {
        return this.getString(Property.JCR_CONTENT);
    }

    @Override
    public void setText(@Nullable String value) throws DocumentException {
        this.setString(Property.JCR_CONTENT, value);
    }

    @Override
    public String getReceiverId()throws DocumentException  {
        try {
            return this.getNode().getParent().getIdentifier();
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Override
    public CommentReceiver getReceiver() throws DocumentException {
        return (JcrCommentReceiver) () -> JcrComment.this.node;
    }

    @Override
    public Long getRank()throws DocumentException  {
        return this.getLong(Namespace.RANK);
    }

    @Override
    public void setRank(@Nullable Long value) throws DocumentException {
        this.setLong(Namespace.RANK, value);
    }

    @Override
    protected void checkCompatibility() throws CompatibilityException {
        this.checkTypeCompatibility(Namespace.COMMENT);
    }

    public void update(@NotNull Comment other) throws DocumentException {
        if (other.getRank() != null) {
            this.setRank(other.getRank());
        }
        if (other.getText() != null) {
            this.setText(other.getText());
        }
    }
}
