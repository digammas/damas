package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFolder;
import solutions.digamma.damas.content.Folder;

/**
 * @author Ahmad Shahwan
 */
public class FolderUpdater extends FileUpdater implements Folder {

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
}
