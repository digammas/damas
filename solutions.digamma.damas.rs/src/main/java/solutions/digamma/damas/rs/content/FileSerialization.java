package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.File;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.Metadata;
import solutions.digamma.damas.rs.common.EntitySerialization;

/**
 * File object serialization.
 *
 * @author Ahmad Shahwan
 */
abstract public class FileSerialization
        extends EntitySerialization
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
     * @param pattern   A copy to follow.
     * @param full      Whether to copy with full details.
     */
    protected FileSerialization(File pattern, boolean full) {
        super(pattern);
        this.name = pattern.getName();
        this.parentId = pattern.getParentId();
        if (full) {
            this.path = pattern.getPath();
            this.createdBy = pattern.getCreatedBy();
            this.creationDate = pattern.getCreationDate();
            this.modifiedBy = pattern.getModifiedBy();
            this.modificationDate = pattern.getModificationDate();
            this.metadata = MetadataSerialization.from(pattern.getMetadata());
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

    public void setMetadata(MetadataSerialization metadata) {
        this.metadata = metadata;
    }

    @Override
    public void updateMetadata(Metadata metadata) {
        this.metadata = MetadataSerialization.from(metadata);
    }

    @Override
    public String getPath() {
        return this.path;
    }

}
