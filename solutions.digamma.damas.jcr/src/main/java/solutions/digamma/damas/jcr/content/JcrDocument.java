package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedDocument;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentPayload;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.jcr.Namespace;
import solutions.digamma.damas.jcr.error.IncompatibleNodeTypeException;
import solutions.digamma.damas.jcr.error.JcrException;
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
    JcrDocument(@NotNull Node node) throws DocumentException {
        super(node);
    }

    /**
     * Construct a new folder given its name and the parent node.
     *
     * @param name
     * @param parent
     * @return
     * @throws DocumentException
     */
    JcrDocument(@NotNull String name, @NotNull Node parent)
            throws DocumentException {
        this(create(name, parent));
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

    public void updateContent(@NotNull InputStream stream)
            throws DocumentException {
        try {
            Binary binary = this
                    .getSession()
                    .getValueFactory()
                    .createBinary(stream);
            this.node.getNode(Property.JCR_CONTENT)
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
    public @NotNull DetailedDocument expand() throws DocumentException {
        return new JcrDetailedDocument(this.node);
    }

    /**
     * Help method to create document's JCR node.
     *
     * @param name      node name
     * @param parent    parent node
     * @return          JCR node
     * @throws DocumentException
     */
    static protected Node create(
            @NotNull String name,
            @NotNull Node parent)
            throws DocumentException {
        try {
            Node node = JcrFile.create(name, Namespace.DOCUMENT, parent);
            node.addNode(Property.JCR_CONTENT, NodeType.NT_RESOURCE);
            return node;
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }
}
