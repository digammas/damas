package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.DetailedFile
import solutions.digamma.damas.content.Metadata
import solutions.digamma.damas.inspection.NotNull
import solutions.digamma.damas.inspection.Nullable
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrCreated
import solutions.digamma.damas.jcr.model.JcrModifiable

import javax.jcr.RepositoryException
import java.net.URI

/**
 * @author Ahmad Shahwan
 */
interface JcrDetailedFile : DetailedFile, JcrCreated, JcrModifiable {


    @Throws(WorkspaceException::class)
    override fun getPath(): String {
        try {
            return URI
                    .create(JcrFile.ROOT_PATH)
                    .relativize(URI.create(this.node.path))
                    .path
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

    }

    @Throws(WorkspaceException::class)
    override fun getMetadata(): Metadata? {
        return null
    }

    @Throws(WorkspaceException::class)
    override fun setMetadata(metadata: Metadata) {

    }
}
