package solutions.digamma.damas.core.content;

import solutions.digamma.damas.api.DocumentException;
import solutions.digamma.damas.api.content.Document;
import solutions.digamma.damas.api.inspection.Nonnull;
import solutions.digamma.damas.core.CompatibilityException;
import solutions.digamma.damas.core.IncompatibleNodeType;

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

    @Override
    protected void checkCompatibility() throws CompatibilityException {
        super.checkCompatibility();
        try {
            if (!this.node.isNodeType(NodeType.NT_FILE)) {
                throw new IncompatibleNodeType(
                        "Node is not of nt:file type.");
            }
        } catch (RepositoryException e) {
            throw new CompatibilityException(e);
        }
    }
}
