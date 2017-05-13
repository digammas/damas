package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFile;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.inspection.Nullable;

/**
 * Base file object updater.
 *
 * @author Ahmad Shahwan
 */
public class FileUpdater implements File {

    private String id;
    private String name;
    private String parentId;

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
    public void setName(@Nonnull String value) throws DocumentException {
        this.name = value;
    }

    @Override
    public @Nullable FolderUpdater getParent() throws DocumentException {
        return null;
    }

    @Override
    public void setParent(@Nonnull Folder value) throws DocumentException {

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
    public @Nonnull DetailedFile expand() throws DocumentException {
        return null;
    }
}
