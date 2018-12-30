package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
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
     * Constructor that mimics another document object.
     *
     * @param copy a copy to mimic
     * @param full whether to copy with full details
     * @throws WorkspaceException
     */
    public DocumentSerialization(Document copy, boolean full)
            throws WorkspaceException {
        super(copy, full);
    }

    @Override
    public List<Version> getVersions() throws WorkspaceException {
        return null;
    }

    @Override
    public List<Comment> getComments() throws WorkspaceException {
        return null;
    }
}
