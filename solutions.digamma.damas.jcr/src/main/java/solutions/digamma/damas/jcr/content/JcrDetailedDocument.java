package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
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
    JcrDetailedDocument(@NotNull Node node) throws DocumentException {
        super(node);
    }

    @Override
    public @NotNull List<@NotNull Version> getVersions() {
        return new ArrayList<>(0);
    }
}
