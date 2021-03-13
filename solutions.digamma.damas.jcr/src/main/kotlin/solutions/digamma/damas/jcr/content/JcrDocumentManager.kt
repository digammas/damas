package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.DocumentManager
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrPathFinder
import solutions.digamma.damas.logging.Logged
import java.io.InputStream
import javax.inject.Singleton
import javax.jcr.RepositoryException

/**
 * JCR implementation convert folder manager.
 *
 * @author Ahmad Shahwan
 */
@Singleton
internal open class JcrDocumentManager :
        JcrCrudManager<Document>(), JcrPathFinder<Document>, DocumentManager {

    @Logged
    @Throws(WorkspaceException::class)
    override fun create(entity: Document, stream: InputStream) =
            Exceptions.check {
        this.doCreate(entity).also { it.updateContent(stream) }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun download(id: String) =
            Exceptions.check {
        val document = this.doRetrieve(id)
        document.content
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun upload(id: String, stream: InputStream) =
            Exceptions.check {
        this.doRetrieve(id).updateContent(stream)
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun copy(sourceId: String, destinationId: String): Document {
        return this.doRetrieve(sourceId).duplicate(destinationId)
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doRetrieve(id: String) =
            JcrDocument.of(this.session.getNodeByIdentifier(id))

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doCreate(pattern: Document): JcrDocument {
        return JcrDocument
                .from(this.session, pattern.parentId, pattern.name)
                .also {
            it.mimeType = pattern.mimeType
        }
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doUpdate(id: String, pattern: Document) =
        this.doRetrieve(id).also { it.update(pattern) }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doDelete(id: String) =
        this.doRetrieve(id).remove()

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doFind(path: String) =
        JcrDocument.of(this.session.getNode(JcrFile.ROOT_PATH).getNode(path))
}
