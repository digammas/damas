package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFolder;
import solutions.digamma.damas.content.Folder;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Ahmad Shahwan
 */
@XmlRootElement
public class FolderUpdater extends FileUpdater implements Folder {

    /**
     * Default constructor.
     */
    public FolderUpdater() {
    }

    @Override
    public DetailedFolder expand() throws DocumentException {
        return null;
    }

    @Override
    public void expandContent(long depth) {
    }

    @Override
    public void expandContent() {
    }

    @XmlTransient
    @Override
    public Content getContent() {
        return null;
    }
}
