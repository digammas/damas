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
import javax.jcr.ItemExistsException
import javax.jcr.Node
import javax.jcr.RepositoryException

/**
 * JCR-based implementation convert file abstract type.
 *
 * @param node              JCR node to capture state
 *
 * @author Ahmad Shahwan
 */
abstract class JcrFile
@Throws(WorkspaceException::class)
protected constructor(node: Node) : JcrBaseEntity(node), File {

    @Throws(InternalStateException::class)
    override fun checkCompatibility() {
        this.checkTypeCompatibility(TypeNamespace.FILE)
        Exceptions.wrap {
            this.node.path.startsWith(ROOT_PATH) ||
                throw InternalStateException("Node not in content root.")
        }
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
    override fun getParent(): Folder? =
        Exceptions.wrap { this.node.parent }?.let { JcrFolder.of(it) }

    @Throws(WorkspaceException::class)
    override fun setParent(value: Folder) {
        this.parentId = value.id ?:
                throw UnsupportedOperationException("Parent ID is null.")
    }

    @Throws(WorkspaceException::class)
    override fun getParentId(): String =
        Exceptions.wrap { this.node.parent.identifier }

    @Throws(WorkspaceException::class)
    override fun setParentId(value: String) = Exceptions.wrap {
        val path = this.session
                .getNodeByIdentifier(value)
                .path + "/"
        val destination = URI
                .create(path)
                .resolve(this.node.name)
                .path
        this.move(destination)
    }

    @Throws(RepositoryException::class)
    private fun move(path: String) {
        try {
            this.node.session.move(this.node.path, path)
        } catch (e: ItemExistsException) {
            throw FileExistsException(path, e)
        }
    }

    /**
     * Update file with file information.
     *
     * @param other
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun update(other: File) {
        other.name?.let { this.name = it }
        other.parentId?.let { this.parentId = it }
    }

    companion object {

        /**
         * Content folder JCR path.
         */
        const val ROOT_PATH = "/content"
    }
}
