package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.DetailedDocument
import solutions.digamma.damas.content.Version
import solutions.digamma.damas.inspection.NotNull

import javax.jcr.Node
import java.util.ArrayList

/**
 * @author Ahmad Shahwan
 */
class JcrDetailedDocument
/**
 * Constructor with JCR node.
 *
 * @param node
 */
@Throws(WorkspaceException::class)
internal constructor(node: Node) : JcrDocument(node), DetailedDocument, JcrDetailedFile, JcrCommentReceiver {


    override fun getVersions(): List<Version> {
        return ArrayList(0)
    }
}
