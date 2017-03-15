package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFolder;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.inspection.Nonnull;
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
        this.checkTypeCompatibility(Namespace.FOLDER);
    }

    @Override
    public @Nonnull DetailedFolder expand() throws DocumentException {
        return new JcrDetailedFolder(this.node);
    }

    @Override
    public void showContent(long depth) {
        this.contentDepth = depth;
    }

    @Override
    public Document @Nonnull [] getDocuments() {
        return null;
    }

    @Override
    public Folder @Nonnull [] getFolders() {
        return null;
    }
}
