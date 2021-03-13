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

/**
 * JCR-based comment manager implementation.
 *
 * @author Ahmad Shahwan
 */
@Singleton
internal open class JcrCommentManager :
        JcrCrudManager<Comment>(), JcrSearchEngine<Comment>, CommentManager {

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doRetrieve(id: String) =
        JcrComment.of(this.session.getNodeByIdentifier(id))

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doCreate(pattern: Comment): JcrComment {
        return JcrComment.from(this.session, pattern.receiverId).also {
            it.update(pattern)
        }
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doUpdate(id: String, pattern: Comment) =
        this.doRetrieve(id).also { it.update(pattern) }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doDelete(id: String) =
        this.doRetrieve(id).remove()

    override fun getNodePrimaryType() = TypeNamespace.COMMENT

    override fun getDefaultRootPath() = JcrFile.ROOT_PATH

    override fun fromNode(node: Node) = JcrComment.of(node)
}
