package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFile;
import solutions.digamma.damas.content.Metadata;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.error.JcrException;
import solutions.digamma.damas.jcr.model.JcrCreated;
import solutions.digamma.damas.jcr.model.JcrModifiable;

import javax.jcr.RepositoryException;
import java.net.URI;

/**
 * @author Ahmad Shahwan
 */
public interface JcrDetailedFile
        extends DetailedFile, JcrCreated, JcrModifiable {

    @Override
    default @NotNull String getPath() throws DocumentException {
        try {
            return URI
                    .create(JcrFile.CONTENT_ROOT)
                    .relativize(URI.create(this.getNode().getPath()))
                    .getPath();
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    @Override
    default @Nullable Metadata getMetadata() throws DocumentException {
        return null;
    }

    @Override
    default void setMetadata(@Nullable Metadata metadata)
            throws DocumentException {

    }
}
