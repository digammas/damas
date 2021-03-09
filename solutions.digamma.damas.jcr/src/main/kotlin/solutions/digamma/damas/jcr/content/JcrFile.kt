package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.common.InternalStateException
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
import javax.jcr.Node
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
        Exceptions.check {
            this.node.path.startsWith(ROOT_PATH) ||
                throw InternalStateException("Node not in content root.")
        }
    }

    override fun getName(): String {
        return Exceptions.uncheck { this.node.name }
    }

    override fun setName(value: String) {
        Exceptions.uncheck {
            /* Use node's path since paths don't end with a slash.
             */
            this.move("${this.node.parent.path}/$value")
        }
    }

    override fun getParent(): Folder? =
        Exceptions.uncheck { this.node.parent }?.let { JcrFolder.of(it) }

    override fun setParent(value: Folder) {
        this.setParentId(value.id)
    }

    override fun getParentId(): String? = Exceptions.uncheck {
        if (ROOT_PATH == this.node.path) null else this.node.parent.identifier
    }

    override fun setParentId(value: String) = Exceptions.uncheck {
        val path = this.session.getNodeByIdentifier(value).path
        this.move("$path/${this.node.name}")
    }

    override fun getPath(): String = Exceptions.uncheck {
        this.relativizePath(this.node.path)
    }

    override fun getPathIds(): List<String> {
        return IntRange(1, this.node.depth)
                .map { this.node.getAncestor(it) }
                .map { if (it is Node) it.identifier else "" }
    }

    override fun getMetadata(): Metadata? = null

    override fun updateMetadata(metadata: Metadata) {}

    @Throws(WorkspaceException::class)
    private fun move(path: String) {
        Exceptions.check {
            this.node.session.move(this.node.path, path)
        }
    }

    @Throws(WorkspaceException::class)
    private fun copy(path: String) {
        Exceptions.check {
            this.node.session.workspace.copy(this.node.path, path)
        }
    }

    @Throws(WorkspaceException::class)
    protected fun copyUnder(parentId: String): String = Exceptions.check {
        var path = this.session.getNodeByIdentifier(parentId).path
        path = "$path/${this.node.name}"
        this.copy(path)
        path
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
        other.parentId?.let { this.setParentId(it) }
        other.metadata?.let { this.updateMetadata(it) }
    }

    /**
     * Grant current user full access rights on this file. This method is called
     * once upon creation.
     */
    protected fun initPermissions() {
        Permissions.selfGrant(this.node, AccessRight.MAINTAIN)
    }

    protected fun relativizePath(path: String): String {
        return path.substring(ROOT_PATH.length)
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
        fun of(node: Node) = Exceptions.check {
            when {
                node.isNodeType(NodeType.NT_FILE) -> JcrDocument.of(node)
                node.isNodeType(NodeType.NT_FOLDER) -> JcrFolder.of(node)
                else -> throw InvalidNodeTypeException(node.path)
            }
        }
    }
}
