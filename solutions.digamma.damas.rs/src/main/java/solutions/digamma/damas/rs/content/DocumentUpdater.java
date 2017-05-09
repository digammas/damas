package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedDocument;
import solutions.digamma.damas.content.Document;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ahmad Shahwan
 */
@XmlRootElement
public class DocumentUpdater extends FileUpdater implements Document {

    /**
     * Default constructor.
     */
    public DocumentUpdater() {
    }

    @Override
    public DetailedDocument expand() throws DocumentException {
        return null;
    }
}
