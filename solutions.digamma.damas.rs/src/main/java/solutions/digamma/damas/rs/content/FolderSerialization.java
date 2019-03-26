package solutions.digamma.damas.rs.content;

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
     */
    private FolderSerialization(Folder copy, boolean full) {
        super(copy, full);
    }

    public FolderSerialization expand() {
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

    /**
     * Create serializable folder from another folder pattern.
     *
     * @param p     Document pattern.
     * @param full  Whether to copy with full details.
     * @return      Serializable copy.
     */
    public static FolderSerialization from(Folder p, boolean full) {
        return p == null ? null : new FolderSerialization(p, full);
    }
}
