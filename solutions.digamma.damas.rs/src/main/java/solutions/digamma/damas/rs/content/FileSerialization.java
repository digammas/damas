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
abstract public class FileSerialization
        extends  EntitySerialization
        implements File {

    private String name;
    private String parentId;
    private String path;
    private MetadataSerialization metadata;

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
            this.metadata = MetadataSerialization.from(copy.getMetadata());
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String value) {
        this.name = value;
    }

    @Override
    public FolderSerialization getParent() {
        return null;
    }

    @Override
    public void setParent(Folder value) {

    }

    @Override
    public String getParentId() {
        return this.parentId;
    }

    @Override
    public void setParentId(String value) {
        this.parentId = value;
    }

    @Override
    public MetadataSerialization getMetadata() {
        return this.metadata;
    }

    @Override
    public void setMetadata(Metadata metadata) throws WorkspaceException {
        this.metadata = MetadataSerialization.from(metadata);
    }

    public void setMetadata(MetadataSerialization metadata) {
        this.metadata = metadata;
    }

    @Override
    public String getPath() {
        return this.path;
    }

}
