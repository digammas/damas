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
internal interface JcrCommentReceiver : CommentReceiver, JcrEntity {

    override fun getComments(): List<Comment> = Exceptions.uncheck {
        val comments = ArrayList<Comment>()
        val nodes = this.getChildNodes(TypeNamespace.COMMENT)
        while (nodes.hasNext()) {
            comments.add(JcrComment.of(nodes.nextNode()))
        }
        Collections.unmodifiableList(comments)
    }
}
