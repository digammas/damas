package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.Folder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Folder object serialization.
 *
 * @author Ahmad Shahwan
 */
@XmlRootElement(name = "Folder")
public class FolderSerialization extends FileSerialization implements Folder {

    private ContentSerialization content;

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
        this.content = ContentSerialization.from(copy.getContent());
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
    public ContentSerialization getContent() {
        return this.content;
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

    /**
     * Create serializable folder from another folder pattern.
     *
     * @param p     Document pattern.
     * @return      Serializable copy.
     */
    public static FolderSerialization from(Folder p) {
        return p == null ? null : new FolderSerialization(p, false);
    }

    /**
     * Create serializable folders list from another folders list.
     *
     * @param list  Folders list.
     * @return      Serializable copy of the list.
     */
    public static List<FolderSerialization> from(
            List<? extends Folder> list) {
        return list == null ? null : list
                .stream()
                .map(FolderSerialization::from)
                .collect(Collectors.toList());
    }
}
