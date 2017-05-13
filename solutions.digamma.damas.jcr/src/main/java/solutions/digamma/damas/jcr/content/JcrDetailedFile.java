package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFile;
import solutions.digamma.damas.content.Metadata;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.error.JcrExceptionMapper;
import solutions.digamma.damas.jcr.model.JcrCreated;
import solutions.digamma.damas.jcr.model.JcrModifiable;

import javax.jcr.RepositoryException;
import java.net.URI;

/**
 * @author Ahmad Shahwan
 */
public interface JcrDetailedFile
        extends DetailedFile, JcrCreated, JcrModifiable, JcrCommentReceiver {

    @Override
    default public @Nonnull String getPath() throws DocumentException {
        try {
            return URI
                    .create(JcrFile.CONTENT_ROOT)
                    .relativize(URI.create(this.getNode().getPath()))
                    .getPath();
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Override
    default public @Nullable Metadata getMetadata() throws DocumentException {
        return null;
    }

    @Override
    default public void setMetadata(@Nullable Metadata metadata)
            throws DocumentException {

    }
}
