package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFolder;
import solutions.digamma.damas.content.Folder;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Folder object serialization.
 *
 * @author Ahmad Shahwan
 */
@XmlRootElement(name = "Folder")
public class FolderSerialization extends FileSerialization implements Folder {

    /**
     * Default constructor.
     */
    public FolderSerialization() {
        super();
    }

    /**
     * A constructor that mimics another folder object.
     *
     * @param copy
     * @throws DocumentException
     */
    public FolderSerialization(Folder copy) throws DocumentException {
        super(copy);
    }

    @Override
    public DetailedFolder expand() {
        return null;
    }

    @Override
    public void expandContent(int depth) {
    }

    @Override
    public void expandContent() {
    }

    @Override
    public Content getContent() {
        return null;
    }

    /**
     * For deserialization purposes.
     *
     * @param value
     */
    public void setContent(Object value) {
    }
}
