package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.auth.Token
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
class JcrDocumentManager :
        JcrCrudManager<Document>(), JcrPathFinder<Document>, DocumentManager {

    @Logged
    @Throws(WorkspaceException::class)
    override fun create(token: Token, entity: Document, stream: InputStream) =
            Exceptions.wrap(this.openSession(token)) {
        val document = this.create(it.getSession(), entity)
        document.updateContent(stream)
        document
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun download(token: Token, id: String) =
            Exceptions.wrap(this.openSession(token)) {
        val document = this.retrieve(it.getSession(), id)
        document.content
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun upload(token: Token, id: String, stream: InputStream) =
            Exceptions.wrap(this.openSession(token)) {
        this.retrieve(it.getSession(), id).updateContent(stream)
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun retrieve(session: Session, id: String) =
            JcrDocument.of(session.getNodeByIdentifier(id))

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun create(session: Session, pattern: Document) =
        JcrDocument.from(session, pattern.parentId, pattern.name)

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
