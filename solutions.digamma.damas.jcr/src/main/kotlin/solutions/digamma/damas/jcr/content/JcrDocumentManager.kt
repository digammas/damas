package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.common.CompatibilityException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.DocumentManager
import solutions.digamma.damas.content.DocumentPayload
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrPathFinder
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.logging.Logged
import java.io.InputStream
import javax.inject.Singleton
import javax.jcr.Property
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.nodetype.NodeType

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
    override fun create(
            token: Token,
            entity: Document,
            stream: InputStream): Document = Exceptions.wrap {
        this.openSession(token).use {
            val document = this.create(it.getSession(), entity)
            document.updateContent(stream)
            document
        }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun download(
            token: Token,
            id: String): DocumentPayload = Exceptions.wrap {
        this.openSession(token).use {
            val document = this.retrieve(it.getSession(), id)
            document.content
        }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun upload(
            token: Token,
            id: String,
            stream: InputStream) {
        Exceptions.wrap {
            this.openSession(token).use {
                this.retrieve(it.getSession(), id).updateContent(stream)
            }
        }
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun retrieve(session: Session, id: String) =
            JcrDocument(session.getNodeByIdentifier(id))

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun create(
            session: Session,
            entity: Document): JcrDocument {
        val name = entity.name
        val parent = session.getNodeByIdentifier(entity.parentId)
        if (!parent.isNodeType(TypeNamespace.FOLDER)) {
            throw CompatibilityException("Parent is not a folder")
        }
        val node = parent.addNode(name, TypeNamespace.DOCUMENT)
        node.addMixin(TypeNamespace.FILE)
        node.addNode(Property.JCR_CONTENT, NodeType.NT_RESOURCE)
        return JcrDocument(node)
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun update(
            session: Session,
            id: String,
            entity: Document): JcrDocument {
        val document = this.retrieve(session, id)
        document.update(entity)
        return document
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun delete(session: Session, id: String) {
        val document = this.retrieve(session, id)
        document.remove()
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun find(session: Session, path: String): Document {
        return JcrDocument(session.getNode(JcrFile.ROOT_PATH).getNode(path))
    }
}
