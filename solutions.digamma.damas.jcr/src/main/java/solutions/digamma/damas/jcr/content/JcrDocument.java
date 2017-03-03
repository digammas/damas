package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.jcr.fail.IncompatibleNodeTypeException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

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
        return new JcrDocument(create(name, NodeType.NT_FILE, parent));
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
}
