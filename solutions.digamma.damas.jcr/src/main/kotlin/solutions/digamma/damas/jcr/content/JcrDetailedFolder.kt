package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.DetailedFolder
import javax.jcr.Node

/**
 * Expanded folder.
 *
 * @author Ahmad Shahwan
 */
class JcrDetailedFolder
/**
 * Constructor with JCR node.
 *
 * @param node      back-bone JCR node
 */
@Throws(WorkspaceException::class)
constructor(node: Node) : JcrFolder(node), DetailedFolder, JcrDetailedFile
