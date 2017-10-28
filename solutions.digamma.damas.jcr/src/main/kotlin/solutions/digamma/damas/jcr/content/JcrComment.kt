package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.CompatibilityException
import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Comment
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrBaseEntity
import solutions.digamma.damas.jcr.model.JcrCreated
import solutions.digamma.damas.jcr.model.JcrModifiable
import solutions.digamma.damas.jcr.names.ItemNamespace
import solutions.digamma.damas.jcr.names.TypeNamespace
import java.util.UUID
import javax.jcr.Node
import javax.jcr.Property
import javax.jcr.Session

/**
 * @author Ahmad Shahwan
 *
 * @param node  comment's JCR node
 *
 * @throws WorkspaceException
 */
class JcrComment
@Throws(WorkspaceException::class)
private constructor(node: Node) :
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
    override fun getReceiverId(): String = Exceptions.wrap {
        this.node.parent.identifier
    }

    @Throws(WorkspaceException::class)
    override fun getReceiver() = { this.node } as JcrCommentReceiver

    @Throws(WorkspaceException::class)
    override fun getRank(): Long? = this.getLong(ItemNamespace.RANK)

    @Throws(WorkspaceException::class)
    override fun setRank(value: Long?) = this.setLong(ItemNamespace.RANK, value)

    @Throws(InternalStateException::class)
    override fun checkCompatibility() =
            this.checkTypeCompatibility(TypeNamespace.COMMENT)

    @Throws(WorkspaceException::class)
    fun update(other: Comment) {
        other.rank?.let { this.rank = it }
        other.text?.let { this.text = it }
    }

    companion object {

        /**
         * Factory method to create comment out of JCR node.
         */
        @Throws(WorkspaceException::class)
        fun of(node: Node) = JcrComment(node)


        @Throws(WorkspaceException::class)
        fun from(session: Session, receiverId: String) = Exceptions.wrap {
            val name = UUID.randomUUID().toString()
            val parent = session.getNodeByIdentifier(receiverId)
            if (!parent.isNodeType(TypeNamespace.COMMENT_RECEIVER)) {
                throw CompatibilityException("Parent cannot receive comments.")
            }
            val node = parent.addNode(name, TypeNamespace.COMMENT)
            JcrComment.of(node)
        }
    }
}
