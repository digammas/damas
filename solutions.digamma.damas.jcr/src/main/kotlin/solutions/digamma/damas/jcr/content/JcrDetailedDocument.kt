package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.DetailedDocument
import solutions.digamma.damas.content.Version
import java.util.ArrayList
import javax.jcr.Node

/**
 * Detailed document.
 *
 * @author Ahmad Shahwan
 *
 * @param node
 */
class JcrDetailedDocument
@Throws(WorkspaceException::class)
private constructor(node: Node) : JcrDocument(node), DetailedDocument,
        JcrDetailedFile, JcrCommentReceiver {

    override fun getVersions(): List<Version> {
        return ArrayList(0)
    }

    companion object {

        @Throws(WorkspaceException::class)
        fun of(node: Node) = JcrDetailedDocument(node)
    }
}
