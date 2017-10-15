package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.DetailedFolder;
import solutions.digamma.damas.inspection.NotNull;

import javax.jcr.Node;

/**
 * Expanded folder.
 *
 * @author Ahmad Shahwan
 */
public class JcrDetailedFolder extends JcrFolder
        implements DetailedFolder, JcrDetailedFile {

    /**
     * Constructor with JCR node.
     *
     * @param node      back-bone JCR node
     */
    public JcrDetailedFolder(@NotNull Node node) throws WorkspaceException {
        super(node);
    }
}
