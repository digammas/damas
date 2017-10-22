package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Comment
import solutions.digamma.damas.content.CommentReceiver
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrBaseEntity
import solutions.digamma.damas.jcr.model.JcrCreated
import solutions.digamma.damas.jcr.model.JcrModifiable
import solutions.digamma.damas.jcr.names.ItemNamespace
import solutions.digamma.damas.jcr.names.TypeNamespace
import javax.jcr.Node
import javax.jcr.Property
import javax.jcr.RepositoryException

/**
 * @author Ahmad Shahwan
 */
class JcrComment
/**
 * No argument constructor.
 *
 * @param node  comment's JCR node
 * @throws WorkspaceException
 */
@Throws(WorkspaceException::class)
internal constructor(node: Node) :
        JcrBaseEntity(node),
        Comment,
        JcrCommentReceiver,
        JcrCreated,
        JcrModifiable {

    @Throws(WorkspaceException::class)
    override fun getText(): String {
        return this.getString(Property.JCR_CONTENT)
    }

    @Throws(WorkspaceException::class)
    override fun setText(value: String) {
        this.setString(Property.JCR_CONTENT, value)
    }

    @Throws(WorkspaceException::class)
    override fun getReceiverId(): String {
        try {
            return this.node.parent.identifier
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

    }

    @Throws(WorkspaceException::class)
    override fun getReceiver(): CommentReceiver {
        return { this@JcrComment.node } as JcrCommentReceiver
    }

    @Throws(WorkspaceException::class)
    override fun getRank(): Long? {
        return this.getLong(ItemNamespace.RANK)
    }

    @Throws(WorkspaceException::class)
    override fun setRank(value: Long?) {
        this.setLong(ItemNamespace.RANK, value)
    }

    @Throws(InternalStateException::class)
    override fun checkCompatibility() {
        this.checkTypeCompatibility(TypeNamespace.COMMENT)
    }

    @Throws(WorkspaceException::class)
    fun update(other: Comment) {
        if (other.rank != null) {
            this.rank = other.rank
        }
        if (other.text != null) {
            this.text = other.text
        }
    }
}
