package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFile;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;

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

    protected FileSerialization(File copy) throws DocumentException {
        this.id = copy.getId();
        this.name = copy.getName();
        this.parentId = copy.getParentId();
    }

    @Override
    public @Nullable String getId() {
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
    public @Nullable String getName() throws DocumentException {
        return this.name;
    }

    @Override
    public void setName(@NotNull String value) throws DocumentException {
        this.name = value;
    }

    @Override
    public @Nullable FolderSerialization getParent() throws DocumentException {
        return null;
    }

    @Override
    public void setParent(@NotNull Folder value) throws DocumentException {

    }

    @Override
    public String getParentId() throws DocumentException {
        return this.parentId;
    }

    @Override
    public void setParentId(String value) throws DocumentException {
        this.parentId = value;
    }

    @Override
    public @NotNull DetailedFile expand() throws DocumentException {
        return null;
    }
}
