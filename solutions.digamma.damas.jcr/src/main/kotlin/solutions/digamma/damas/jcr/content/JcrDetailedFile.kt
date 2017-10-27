package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.DetailedFile
import solutions.digamma.damas.content.Metadata
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrCreated
import solutions.digamma.damas.jcr.model.JcrModifiable
import java.net.URI
import javax.jcr.RepositoryException

/**
 * @author Ahmad Shahwan
 */
interface JcrDetailedFile : DetailedFile, JcrCreated, JcrModifiable {


    @Throws(WorkspaceException::class)
    override fun getPath(): String = Exceptions.wrap {
        URI.create(JcrFile.ROOT_PATH)
                .relativize(URI.create(this.node.path)).path
    }

    @Throws(WorkspaceException::class)
    override fun getMetadata(): Metadata? = null

    @Throws(WorkspaceException::class)
    override fun setMetadata(metadata: Metadata) {}
}
