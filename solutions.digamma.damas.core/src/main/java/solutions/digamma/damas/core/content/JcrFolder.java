package solutions.digamma.damas.core.content;

import solutions.digamma.damas.api.DocumentException;
import solutions.digamma.damas.api.content.Folder;
import solutions.digamma.damas.api.inspection.Nonnull;
import solutions.digamma.damas.core.CompatibilityException;
import solutions.digamma.damas.core.IncompatibleNodeType;
import solutions.digamma.damas.core.IncompatiblePathException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

/**
 * JCR-based folder implementation.
 *
 * @author Ahmad Shahwan
 */
public class JcrFolder extends JcrFile implements Folder {

    /**
     * Constructor.
     *
     * @param node
     */
    public JcrFolder(@Nonnull Node node) throws DocumentException {
        super(node);
    }

    @Override
    protected void checkCompatibility() throws CompatibilityException {
        super.checkCompatibility();
        try {
            if (!this.node.isNodeType(NodeType.NT_FOLDER)) {
                throw new IncompatibleNodeType(
                        "Node is not of nt:folder type.");
            }
        } catch (RepositoryException e) {
            throw new CompatibilityException(e);
        }
    }
}
