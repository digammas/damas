package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedDocument;
import solutions.digamma.damas.content.Version;
import solutions.digamma.damas.inspection.NotNull;

import javax.jcr.Node;

/**
 * @author Ahmad Shahwan
 */
public class JcrDetailedDocument extends JcrDocument
        implements DetailedDocument, JcrDetailedFile{

    /**
     * Constructor with JCR node.
     *
     * @param node
     */
    public JcrDetailedDocument(@NotNull Node node) throws DocumentException {
        super(node);
    }

    @Override
    public Version @NotNull [] getVersions() {
        return new Version[0];
    }
}
