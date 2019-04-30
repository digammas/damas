package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.Version;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Document object serialization.
 *
 * @author Ahmad Shahwan
 */
@XmlRootElement(name = "Document")
public class DocumentSerialization
        extends FileSerialization implements Document {

    private String mimeType;

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
        this.mimeType = pattern.getMimeType();
    }

    @Override
    public String getMimeType() {
        return this.mimeType;
    }

    @Override
    public void setMimeType(String value) {
        if (value != null) {
            this.mimeType = value;
        }
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

    /**
     * Create serializable document from another document pattern.
     *
     * @param p     Document pattern.
     * @return      Serializable copy.
     */
    public static DocumentSerialization from(Document p) {
        return DocumentSerialization.from(p, false);
    }

    /**
     * Create serializable documents list from another documents list.
     *
     * @param list  Documents list.
     * @return      Serializable copy of the list.
     */
    public static List<DocumentSerialization> from(
            List<? extends Document> list) {
        return list == null ? null : list
                .stream()
                .map(DocumentSerialization::from)
                .collect(Collectors.toList());
    }
}
