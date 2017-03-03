package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.CompatibilityException;

import javax.jcr.Node;
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

    /**
     * Create new folder, given parent node and name.
     *
     * @param name
     * @param parent
     * @return
     * @throws DocumentException
     */
    static JcrFolder create(@Nonnull String name, @Nonnull Node parent)
        throws DocumentException {
        return new JcrFolder(create(name, NodeType.NT_FOLDER, parent));
    }

    @Override
    protected void checkCompatibility() throws CompatibilityException {
        super.checkCompatibility();
        this.checkTypeCompatibility(NodeType.NT_FOLDER);
    }
}
