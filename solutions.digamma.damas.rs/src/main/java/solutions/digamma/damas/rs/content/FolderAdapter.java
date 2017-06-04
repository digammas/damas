package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.rs.BaseAdapter;

/**
 * @author Ahmad Shahwan
 */
public class FolderAdapter extends BaseAdapter<FolderSerialization, Folder> {

    @Override
    public FolderSerialization marshal(Folder value) throws Exception {
        return new FolderSerialization(value);
    }
}
