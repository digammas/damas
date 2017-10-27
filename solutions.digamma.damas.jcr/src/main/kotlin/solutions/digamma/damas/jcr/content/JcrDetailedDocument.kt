package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.DetailedDocument
import solutions.digamma.damas.content.Version
import java.util.ArrayList
import javax.jcr.Node

/**
 * @author Ahmad Shahwan
 *
 * @param node
 */
class JcrDetailedDocument
@Throws(WorkspaceException::class)
internal constructor(node: Node) : JcrDocument(node), DetailedDocument,
        JcrDetailedFile, JcrCommentReceiver {

    override fun getVersions(): List<Version> {
        return ArrayList(0)
    }
}
