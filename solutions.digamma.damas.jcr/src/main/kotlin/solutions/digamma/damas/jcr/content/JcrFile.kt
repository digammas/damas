package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.UnsupportedActionException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.File
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.content.Metadata
import solutions.digamma.damas.jcr.auth.Permissions
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.InvalidNodeTypeException
import solutions.digamma.damas.jcr.model.JcrBaseEntity
import solutions.digamma.damas.jcr.model.JcrCreated
import solutions.digamma.damas.jcr.model.JcrModifiable
import solutions.digamma.damas.jcr.names.TypeNamespace
import java.net.URI
import javax.jcr.ItemExistsException
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.nodetype.NodeType

/**
 * JCR-based implementation convert file abstract type.
 *
 * @param node              JCR node to capture state
 *
 * @author Ahmad Shahwan
 */
internal abstract class JcrFile
@Throws(WorkspaceException::class)
protected constructor(node: Node) : JcrBaseEntity(node),
        JcrCreated, JcrModifiable, File {

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
                throw UnsupportedActionException("Parent ID is null.")
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

    @Throws(WorkspaceException::class)
    override fun getPath(): String = Exceptions.wrap {
        URI.create(JcrFile.ROOT_PATH)
                .relativize(URI.create(this.node.path)).path
    }

    @Throws(WorkspaceException::class)
    override fun getMetadata(): Metadata? = null

    @Throws(WorkspaceException::class)
    override fun setMetadata(metadata: Metadata) {}

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
        other.metadata?.let { this.setMetadata(it) }
    }

    /**
     * Grant current user full access rights on this file. This method is called
     * once upon creation.
     */
    protected fun initPermissions() {
        Permissions.selfGrant(this.node, AccessRight.MAINTAIN)
    }

    companion object {

        /**
         * Content folder JCR path.
         */
        const val ROOT_PATH = "/content"

        /**
         * Retrieve file from JCR node.
         */
        @Throws(WorkspaceException::class)
        fun of(node: Node) = Exceptions.wrap {
            when {
                node.isNodeType(NodeType.NT_FILE) -> JcrDocument.of(node)
                node.isNodeType(NodeType.NT_FOLDER) -> JcrFolder.of(node)
                else -> throw InvalidNodeTypeException(node.path)
            }
        }
    }
}
