package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.UnsupportedOperationException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.File
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrBaseEntity
import solutions.digamma.damas.jcr.names.TypeNamespace
import java.net.URI
import javax.jcr.Node
import javax.jcr.RepositoryException

/**
 * JCR-based implementation convert file abstract type.
 *
 * @author Ahmad Shahwan
 */
abstract class JcrFile
/**
 * Constructor.
 *
 * @param node
 */
@Throws(WorkspaceException::class)
internal constructor(node: Node) : JcrBaseEntity(node), File {

    @Throws(InternalStateException::class)
    override fun checkCompatibility() {
        this.checkTypeCompatibility(TypeNamespace.FILE)
        Exceptions.wrap( {
            if (!this.node.path.startsWith(ROOT_PATH)) {
                throw InternalStateException("Node not in content root.")
            }
        })
    }

    @Throws(WorkspaceException::class)
    override fun getName(): String {
        return Exceptions.wrap { this.node.name }
    }

    @Throws(WorkspaceException::class)
    override fun setName(value: String) {
        Exceptions.wrap {
            /* Use node's path since paths don't end with a slash.
             */
            val destination = URI
                    .create(this.node.path)
                    .resolve(value)
                    .path
            this.move(destination)
        }
    }

    @Throws(WorkspaceException::class)
    override fun getParent(): Folder? {
        val parent = Exceptions.wrap { this.node.parent }
        return if (parent == null) File.NO_PARENT else JcrFolder(parent)
    }

    @Throws(WorkspaceException::class)
    override fun setParent(value: Folder) {
        val id = value.id ?:
                throw UnsupportedOperationException("Parent ID is null.")
        this.parentId = id
    }

    @Throws(WorkspaceException::class)
    override fun getParentId(): String {
        return Exceptions.wrap { this.node.parent.identifier }
    }

    @Throws(WorkspaceException::class)
    override fun setParentId(value: String) {
        Exceptions.wrap {
            val path = this.session
                    .getNodeByIdentifier(value)
                    .path + "/"
            val destination = URI
                    .create(path)
                    .resolve(this.node.name)
                    .path
            this.move(destination)
        }
    }

    @Throws(RepositoryException::class)
    private fun move(path: String) {
        this.node.session.move(this.node.path, path)
    }

    companion object {

        /**
         * Content folder JCR path.
         */
        const val ROOT_PATH = "/content"
    }
}
