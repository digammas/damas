package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.DetailedDocument;
import solutions.digamma.damas.content.Version;
import solutions.digamma.damas.inspection.NotNull;

import javax.jcr.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ahmad Shahwan
 */
public class JcrDetailedDocument extends JcrDocument
        implements DetailedDocument, JcrDetailedFile, JcrCommentReceiver{

    /**
     * Constructor with JCR node.
     *
     * @param node
     */
    JcrDetailedDocument(@NotNull Node node) throws WorkspaceException {
        super(node);
    }

    @Override
    public @NotNull List<@NotNull Version> getVersions() {
        return new ArrayList<>(0);
    }
}
