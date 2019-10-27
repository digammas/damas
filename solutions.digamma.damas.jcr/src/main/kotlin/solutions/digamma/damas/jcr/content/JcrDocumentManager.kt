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
import javax.jcr.Session

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
        this.create(this.getSession(), entity).also { it.updateContent(stream) }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun download(id: String) =
            Exceptions.check {
        val document = this.retrieve(getSession(), id)
        document.content
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun upload(id: String, stream: InputStream) =
            Exceptions.check {
        this.retrieve(this.getSession(), id).updateContent(stream)
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun copy(sourceId: String, destinationId: String): Document {
        return this.retrieve(getSession(), sourceId).duplicate(destinationId)
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun retrieve(session: Session, id: String) =
            JcrDocument.of(session.getNodeByIdentifier(id))

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun create(session: Session, pattern: Document): JcrDocument {
        return JcrDocument.from(session, pattern.parentId, pattern.name).also {
            it.mimeType = pattern.mimeType
        }
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun update(session: Session, id: String, pattern: Document) =
        this.retrieve(session, id).also { it.update(pattern) }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun delete(session: Session, id: String) =
        this.retrieve(session, id).remove()

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun find(session: Session, path: String) =
        JcrDocument.of(session.getNode(JcrFile.ROOT_PATH).getNode(path))
}
