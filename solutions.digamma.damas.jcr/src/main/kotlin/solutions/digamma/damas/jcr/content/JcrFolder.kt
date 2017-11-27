package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.jcr.auth.Permissions
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.TypeNamespace
import java.util.Collections
import javax.jcr.Node
import javax.jcr.Session

/**
 * JCR-based folder implementation.
 *
 * @constructor Private construct folder with a JCR node.
 * @param node
 *
 * @author Ahmad Shahwan
 */
internal open class JcrFolder
@Throws(WorkspaceException::class)
protected constructor(node: Node) : JcrFile(node), Folder {

    /**
     * Content depth used to indicate to which depth is content returned with
     * method `getContent()`. If negative, content will be returned down
     * to leaves. If zero, no content is returned at all. Zero is the default
     * value.
     */
    private var contentDepth = 0

    /**
     * Folder content. If null, the content is not visited yet.
     */
    private var content: Folder.Content? = null

    @Throws(InternalStateException::class)
    override fun checkCompatibility() {
        super.checkCompatibility()
        this.checkTypeCompatibility(TypeNamespace.FOLDER)
    }

    @Throws(WorkspaceException::class)
    override fun expand() = JcrDetailedFolder.of(this.node)

    override fun expandContent(depth: Int) {
        if (depth != this.contentDepth) {
            this.content = null
            this.contentDepth = depth
        }
    }

    override fun expandContent() {
        if (this.contentDepth >= 0) {
            this.content = null
            this.contentDepth = -1
        }
    }

    @Throws(WorkspaceException::class)
    override fun getContent(): Folder.Content? {
        if (this.contentDepth == 0) {
            this.content = null
            return this.content
        }
        if (this.content != null) {
            this.content
            return this.content
        }
        val documents = ArrayList<JcrDocument>()
        val folders = ArrayList<JcrFolder>()
        Exceptions.wrap {
            val iterator = this.node.nodes
            while (iterator.hasNext()) {
                val node = iterator.nextNode()
                if (node.isNodeType(TypeNamespace.DOCUMENT)) {
                    documents.add(JcrDocument.of(node))
                } else if (node.isNodeType(TypeNamespace.FOLDER)) {
                    val folder = JcrFolder.of(node)
                    folder.expandContent(this.contentDepth - 1)
                    folders.add(folder)
                }
            }
        }

        this.content = object : Folder.Content {

            override fun getFolders() =
                    Collections.unmodifiableList(folders)

            override fun getDocuments() =
                    Collections.unmodifiableList(documents)
        }
        return this.content
    }

    companion object {

        @Throws(WorkspaceException::class)
        fun of(node: Node) = JcrFolder(node)

        @Throws(WorkspaceException::class)
        fun from(session: Session, parentId: String, name: String) =
                Exceptions.wrap {
            val parent = session.getNodeByIdentifier(parentId)
            if (parent.hasNode(name)) {
                throw FileExistsException(name)
            }
            val node = parent.addNode(name, TypeNamespace.FOLDER)
            node.addMixin(TypeNamespace.FILE)
            Permissions.selfGrant(node, AccessRight.all())
            JcrFolder.of(node)
        }
    }
}
