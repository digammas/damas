package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFolder;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.jcr.Namespace;

import javax.jcr.Node;
import javax.jcr.nodetype.NodeType;

/**
 * JCR-based folder implementation.
 *
 * @author Ahmad Shahwan
 */
public class JcrFolder extends JcrFile implements Folder {

    private long contentDepth = 0;

    /**
     * Constructor.
     *
     * @param node
     */
    public JcrFolder(@NotNull Node node) throws DocumentException {
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
    static JcrFolder create(@NotNull String name, @NotNull Node parent)
        throws DocumentException {
        return new JcrFolder(create(name, NodeType.NT_FOLDER, parent));
    }

    @Override
    protected void checkCompatibility() throws CompatibilityException {
        super.checkCompatibility();
        this.checkTypeCompatibility(Namespace.FOLDER);
    }

    @Override
    public @NotNull DetailedFolder expand() throws DocumentException {
        return new JcrDetailedFolder(this.node);
    }

    @Override
    public void expandContent(long depth) {
        this.contentDepth = depth;
    }

    @Override
    public void expandContent() {
        this.contentDepth = -1;
    }

    @Override
    public Content getContent() {
        return null;
    }
}
