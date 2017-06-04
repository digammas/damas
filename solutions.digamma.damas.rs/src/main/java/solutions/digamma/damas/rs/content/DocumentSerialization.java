package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedDocument;
import solutions.digamma.damas.content.Document;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Document object serialization.
 *
 * @author Ahmad Shahwan
 */
@XmlRootElement(name = "Document")
public class DocumentSerialization
        extends FileSerialization implements Document {

    /**
     * Default constructor.
     */
    public DocumentSerialization() {
    }

    /**
     * Constructor that mimics another document object.
     *
     * @param copy a copy to mimic.
     * @throws DocumentException
     */
    public DocumentSerialization(Document copy) throws DocumentException {
        super(copy);
    }

    @Override
    public DetailedDocument expand() throws DocumentException {
        return null;
    }
}