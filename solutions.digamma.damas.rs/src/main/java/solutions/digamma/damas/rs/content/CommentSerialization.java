package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.CommentReceiver;
import solutions.digamma.damas.rs.common.EntitySerialization;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "Comment")
public class CommentSerialization
        extends EntitySerialization
        implements Comment {

    private String text;
    private String receiverId;
    private Long rank;
    private List<CommentSerialization> comments;

    /**
     * No-args constructor.
     */
    public CommentSerialization() {
    }

    /**
     * private copy constructor.
     *
     * @param pattern   A pattern to follow.
     * @throws WorkspaceException
     */
    private CommentSerialization(Comment pattern) throws WorkspaceException {
        this.text = pattern.getText();
        this.rank = pattern.getRank();
        this.receiverId = pattern.getReceiverId();
        this.comments = copy(pattern.getComments());
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String value) {
        this.text = value;
    }

    @Override
    public String getReceiverId() {
        return this.receiverId;
    }

    @Override
    public CommentReceiver getReceiver() {
        return null;
    }

    @Override
    public Long getRank() {
        return this.rank;
    }

    @Override
    public void setRank(Long value) {
        this.rank = value;
    }

    @Override
    public List<Comment> getComments() {
        return this.comments == null ? null : List.copyOf(this.comments);
    }

    /**
     * Copy a comment into its serialized counterpart.
     * @param p A pattern to copy.
     * @return  A serialized version of the pattern.
     * @throws WorkspaceException
     */
    public static CommentSerialization copy(Comment p)
            throws WorkspaceException {
        return p == null ? null : new CommentSerialization(p);
    }

    /**
     * Copy a list of {@link Comment} into a list of
     * {@link CommentSerialization}.
     *
     * @param p A pattern to copy.
     * @return  A copied list
     * @throws WorkspaceException
     */
    public static List<CommentSerialization> copy(List<Comment> p)
            throws WorkspaceException {
        try {
            return p == null ? null : p.stream()
                    .map(CommentSerialization::copyOrKeep)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            if (e.getCause() instanceof WorkspaceException) {
                throw (WorkspaceException) e.getCause();
            }
            throw e;
        }
    }

    /**
     * Copy the comment if it is not of type {@link CommentSerialization}.
     * Otherwise return the passed value as is.
     *
     * This method also wraps any {@link WorkspaceException} into an unchecked
     * exception.
     *
     * @param p The pattern to copy.
     * @return  A serialized version.
     */
    private static CommentSerialization copyOrKeep(Comment p) {
        if (p instanceof CommentSerialization) {
            return (CommentSerialization) p;
        }
        try {
            return copy(p);
        } catch (WorkspaceException e) {
            throw new RuntimeException(e);
        }
    }
}
