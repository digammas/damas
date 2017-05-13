package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedDocument;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentPayload;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.jcr.Namespace;
import solutions.digamma.damas.jcr.error.IncompatibleNodeTypeException;
import solutions.digamma.damas.jcr.error.JcrExceptionMapper;

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
    public JcrDocument(@Nonnull Node node) throws DocumentException {
        super(node);
    }

    /**
     * Create a new folder given its name and the parent node.
     *
     * @param name
     * @param parent
     * @return
     * @throws DocumentException
     */
    static JcrDocument create(@Nonnull String name, @Nonnull Node parent)
            throws DocumentException {
        return new JcrDocument(create(name, Namespace.DOCUMENT, parent));
    }

    public DocumentPayload getContent() throws DocumentException {
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
            throw JcrExceptionMapper.map(e);
        }
    }

    public void updateContent(@Nonnull InputStream stream) throws DocumentException {
        try {
            Binary binary = this
                    .getSession()
                    .getValueFactory()
                    .createBinary(stream);
            this.node
                    .getNode(Property.JCR_CONTENT)
                    .setProperty(Property.JCR_DATA, binary);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Override
    protected void checkCompatibility() throws CompatibilityException {
        super.checkCompatibility();
        try {
            if (!this.node.isNodeType(NodeType.NT_FILE)) {
                throw new IncompatibleNodeTypeException(
                        "Node is not of nt:file type.");
            }
        } catch (RepositoryException e) {
            throw new CompatibilityException(e);
        }
    }

    @Override
    public @Nonnull DetailedDocument expand() throws DocumentException {
        return new JcrDetailedDocument(this.node);
    }
}
