package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.Metadata;

import java.util.Calendar;

/**
 * File object serialization.
 *
 * @author Ahmad Shahwan
 */
abstract public class FileSerialization implements File {

    private String id;
    private String name;
    private String parentId;
    private String path;
    private String createdBy;
    private Calendar creationDate;
    private String modifiedBy;
    private Calendar modificationDate;

    /**
     * No-argument constructor.
     */
    protected FileSerialization() {
    }

    /**
     * Copy constructor.
     *
     * @param copy a copy to mimic
     * @param full whether to copy with full details
     * @throws WorkspaceException
     */
    protected FileSerialization(File copy, boolean full)
            throws WorkspaceException {
        this.id = copy.getId();
        this.name = copy.getName();
        this.parentId = copy.getParentId();
        if (full) {
            this.path = copy.getPath();
            this.createdBy = copy.getCreatedBy();
            this.creationDate = copy.getCreationDate();
            this.modifiedBy = copy.getModifiedBy();
            this.modificationDate = copy.getModificationDate();
        }
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
    public MetadataSerialization getMetadata() throws WorkspaceException {
        return null;
    }

    @Override
    public void setMetadata(Metadata metadata) throws WorkspaceException {

    }

    @Override
    public String getPath() throws WorkspaceException {
        return this.path;
    }

    @Override
    public String getCreatedBy() throws WorkspaceException {
        return this.createdBy;
    }

    @Override
    public Calendar getCreationDate() throws WorkspaceException {
        return this.creationDate;
    }

    @Override
    public String getModifiedBy() throws WorkspaceException {
        return this.modifiedBy;
    }

    @Override
    public Calendar getModificationDate() throws WorkspaceException {
        return this.modificationDate;
    }
}
