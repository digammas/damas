package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFolder;
import solutions.digamma.damas.inspection.Nonnull;

import javax.jcr.Node;

/**
 * @author Ahmad Shahwan
 */
public class JcrDetailedFolder extends JcrFolder
        implements DetailedFolder, JcrDetailedFile {

    /**
     * Constructor with JCR node.
     *
     * @param node
     */
    public JcrDetailedFolder(@Nonnull Node node) throws DocumentException {
        super(node);
    }
}
