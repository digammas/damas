package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Comment
import solutions.digamma.damas.content.CommentReceiver
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrEntity
import solutions.digamma.damas.jcr.names.TypeNamespace
import java.util.ArrayList
import java.util.Collections

/**
 * JCR comment receiver.
 *
 * @author Ahmad Shahwan
 */
interface JcrCommentReceiver : CommentReceiver, JcrEntity {

    @Throws(WorkspaceException::class)
    override fun getComments(): List<Comment> {
        val comments = ArrayList<Comment>()
        Exceptions.wrap {
            val nodes = this.getChildNodes(TypeNamespace.COMMENT)
            while (nodes.hasNext()) {
                comments.add(JcrComment.of(nodes.nextNode()))
            }
        }
        return Collections.unmodifiableList(comments)
    }
}
