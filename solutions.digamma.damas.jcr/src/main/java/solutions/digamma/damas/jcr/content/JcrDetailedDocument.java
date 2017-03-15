package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedDocument;
import solutions.digamma.damas.content.Version;
import solutions.digamma.damas.inspection.Nonnull;

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
    public JcrDetailedDocument(@Nonnull Node node) throws DocumentException {
        super(node);
    }

    @Override
    public Version @Nonnull [] getVersions() {
        return new Version[0];
    }
}
