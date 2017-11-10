package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.DetailedFile;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.content.Folder;

/**
 * File object serialization.
 *
 * @author Ahmad Shahwan
 */
abstract public class FileSerialization implements File {

    private String id;
    private String name;
    private String parentId;

    protected FileSerialization() {
    }

    protected FileSerialization(File copy) throws WorkspaceException {
        this.id = copy.getId();
        this.name = copy.getName();
        this.parentId = copy.getParentId();
    }

    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Set ID.
     * 
     * @param value
     */
    public void setId(String value) {
        this.id = value;
    }

    @Override
    public String getName() throws WorkspaceException {
        return this.name;
    }

    @Override
    public void setName(String value) throws WorkspaceException {
        this.name = value;
    }

    @Override
    public FolderSerialization getParent() throws WorkspaceException {
        return null;
    }

    @Override
    public void setParent(Folder value) throws WorkspaceException {

    }

    @Override
    public String getParentId() throws WorkspaceException {
        return this.parentId;
    }

    @Override
    public void setParentId(String value) throws WorkspaceException {
        this.parentId = value;
    }

    @Override
    public DetailedFile expand() throws WorkspaceException {
        return null;
    }
}
