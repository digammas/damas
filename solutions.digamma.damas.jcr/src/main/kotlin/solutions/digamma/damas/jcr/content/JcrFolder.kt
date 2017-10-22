package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.DetailedFolder
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.inspection.NotNull
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.TypeNamespace
import java.util.Collections
import javax.jcr.Node
import javax.jcr.RepositoryException

/**
 * JCR-based folder implementation.
 *
 * @author Ahmad Shahwan
 */
open class JcrFolder
/**
 * Construct folder with a JCR node.
 *
 * @param node
 */
@Throws(WorkspaceException::class)
internal constructor(node: Node) : JcrFile(node), Folder {

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
    override fun expand(): DetailedFolder {
        return JcrDetailedFolder(this.node)
    }

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
        try {
            val iterator = this.node.nodes
            while (iterator.hasNext()) {
                val node = iterator.nextNode()
                if (node.isNodeType(TypeNamespace.DOCUMENT)) {
                    documents.add(JcrDocument(node))
                } else if (node.isNodeType(TypeNamespace.FOLDER)) {
                    val folder = JcrFolder(node)
                    folder.expandContent(this.contentDepth - 1)
                    folders.add(folder)
                }
            }
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

        this.content = object : Folder.Content {

            override fun getFolders(): List<JcrFolder> {
                return Collections.unmodifiableList(folders)
            }

            override fun getDocuments(): List<JcrDocument> {
                return Collections.unmodifiableList(documents)
            }
        }

        return this.content
    }
}
