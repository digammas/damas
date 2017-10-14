package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.InternalStateException;
import solutions.digamma.damas.content.DetailedDocument;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentPayload;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.error.JcrException;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import java.io.InputStream;

/**
 * JCR-based implementation of document.
 *
 * @author Ahmad Shahwan
 */
public class JcrDocument extends JcrFile implements Document {

    /**
     * Constructor.
     *
     * @param node
     */
    JcrDocument(@NotNull Node node) throws WorkspaceException {
        super(node);
    }

    public DocumentPayload getContent() throws WorkspaceException {
        try {
            Binary binary = this.node
                    .getNode(Node.JCR_CONTENT)
                    .getProperty(Property.JCR_DATA)
                    .getBinary();
            InputStream stream = binary.getStream();
            long size = binary.getSize();
            return new DocumentPayload() {
                @Override
                public long getSize() {
                    return size;
                }

                @Override
                public InputStream getStream() {
                    return stream;
                }
            };
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    void updateContent(@NotNull InputStream stream)
            throws WorkspaceException {
        try {
            Binary binary = this
                    .getSession()
                    .getValueFactory()
                    .createBinary(stream);
            this.node.getNode(Property.JCR_CONTENT)
                    .setProperty(Property.JCR_DATA, binary);
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    @Override
    protected void checkCompatibility() throws InternalStateException {
        super.checkCompatibility();
        try {
            if (!this.node.isNodeType(NodeType.NT_FILE)) {
                throw new InternalStateException(
                        "Node is not of nt:file type.");
            }
        } catch (RepositoryException e) {
            throw new InternalStateException(e);
        }
    }

    @Override
    public @NotNull DetailedDocument expand() throws WorkspaceException {
        return new JcrDetailedDocument(this.node);
    }
}
