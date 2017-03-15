package solutions.digamma.damas.content;

import solutions.digamma.damas.Created;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Modifiable;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.inspection.Nullable;

/**
 * @author Ahmad Shahwan
 */
public interface DetailedFile
        extends File, CommentReceiver, Created, Modifiable {

    /**
     * File's metadata.
     *
     * @return
     * @throws DocumentException
     */
    @Nullable Metadata getMetadata() throws DocumentException;

    /**
     * Update file's metadata. If value is null, delete metadata. Only present
     * properties of metadata will be updated. Absent properties remain
     * unchanged.
     *
     * @param metadata
     * @throws DocumentException
     */
    void setMetadata(@Nullable Metadata metadata) throws DocumentException;

    /**
     * Parent path.
     * A path is composed of node names, starting from the root node represented
     * as an empty string, down to the current node. Names are separated by
     * forward slashes.
     *
     * @return
     */
    @Nullable String getPath() throws DocumentException;

    /**
     * Update file with file information.
     *
     * @param other
     * @throws DocumentException
     */
    default void update(@Nonnull DetailedFile other) throws DocumentException {
        File.super.update(other);
        if (other.getMetadata() != null) {
            this.setMetadata(other.getMetadata());
        }
    }
}
