package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Comment
import solutions.digamma.damas.content.CommentManager
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import solutions.digamma.damas.jcr.names.TypeNamespace
import javax.inject.Singleton
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * JCR-based comment manager implementation.
 *
 * @author Ahmad Shahwan
 */
@Singleton
internal open class JcrCommentManager :
        JcrCrudManager<Comment>(), JcrSearchEngine<Comment>, CommentManager {

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun retrieve(session: Session, id: String) =
        JcrComment.of(session.getNodeByIdentifier(id))

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun create(session: Session, pattern: Comment) =
        JcrComment.from(session, pattern.receiverId).also { it.update(pattern) }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun update(
            session: Session, id: String,
            pattern: Comment) =
        this.retrieve(session, id).also { it.update(pattern) }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun delete(session: Session, id: String) =
        this.retrieve(session, id).remove()

    override fun getNodePrimaryType() = TypeNamespace.COMMENT

    override fun getDefaultRootPath() = JcrFile.ROOT_PATH

    override fun fromNode(node: Node) = JcrComment.of(node)
}
