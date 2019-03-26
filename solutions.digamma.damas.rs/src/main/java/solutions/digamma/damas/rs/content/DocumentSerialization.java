package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.Version;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Document object serialization.
 *
 * @author Ahmad Shahwan
 */
@XmlRootElement(name = "Document")
public class DocumentSerialization
        extends FileSerialization implements Document {

    /**
     * Default constructor.
     */
    public DocumentSerialization() {
    }

    /**
     * Constructor that copies another document object.
     *
     * @param pattern   A pattern to Copy.
     * @param full      Whether to copy with full details.
     */
    private DocumentSerialization(Document pattern, boolean full) {
        super(pattern, full);
    }

    @Override
    public List<Version> getVersions() {
        return null;
    }

    @Override
    public List<Comment> getComments() {
        return null;
    }

    /**
     * Create serializable document from another document pattern.
     *
     * @param p     Document pattern.
     * @param full  Whether to copy with full details.
     * @return      Serializable copy.
     */
    public static DocumentSerialization from(Document p, boolean full) {
        return p == null ? null : new DocumentSerialization(p, full);
    }
}
