package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.DetailedFolder
import javax.jcr.Node

/**
 * Expanded folder.
 *
 * @param node              back-bone JCR node
 *
 * @author Ahmad Shahwan
 */
internal class JcrDetailedFolder
@Throws(WorkspaceException::class)
private constructor(node: Node) : JcrFolder(node),
        DetailedFolder, JcrDetailedFile {

    companion object {

        @Throws(WorkspaceException::class)
        fun of(node: Node) = JcrDetailedFolder(node)
    }
}
