package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.CompatibilityException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Comment
import solutions.digamma.damas.content.CommentManager
import solutions.digamma.damas.entity.Page
import solutions.digamma.damas.jcr.common.ResultPage
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import solutions.digamma.damas.jcr.names.TypeNamespace
import java.util.Collections
import java.util.UUID
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * JCR-based comment manager implementation.
 *
 * @author Ahmad Shahwan
 */
class JcrCommentManager :
        JcrCrudManager<Comment>(), JcrSearchEngine<Comment>, CommentManager {

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun retrieve(session: Session, id: String): JcrComment {
        return JcrComment(session.getNodeByIdentifier(id))
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun create(session: Session, entity: Comment): JcrComment {
        val name = UUID.randomUUID().toString()
        val parent = session.getNodeByIdentifier(entity.receiverId)
        if (!parent.isNodeType(TypeNamespace.COMMENT_RECEIVER)) {
            throw CompatibilityException("Parent cannot receive comments.")
        }
        val node = parent.addNode(name, TypeNamespace.COMMENT)
        val comment = JcrComment(node)
        comment.update(entity)
        return comment
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun update(
            session: Session, id: String,
            entity: Comment): JcrComment {
        val comment = this.retrieve(session, id)
        comment.update(entity)
        return comment
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun delete(session: Session, id: String) {
        val comment = this.retrieve(session, id)
        comment.remove()
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun find(
            session: Session,
            offset: Int,
            size: Int,
            query: Any?): Page<Comment> =
            ResultPage(Collections.emptyList(), 0, 0)
}
