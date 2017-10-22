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
        try {
            return this.node.name
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

    }

    @Throws(WorkspaceException::class)
    override fun setName(value: String) {
        try {
            /* Use node's path since paths don't end with a slash.
             */
            val destination = URI
                    .create(this.node.path)
                    .resolve(value)
                    .path
            this.move(destination)
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

    }

    @Throws(WorkspaceException::class)
    override fun getParent(): Folder? {
        val parent: Node?
        try {
            parent = this.node.parent
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

        return if (parent == null) {
            File.NO_PARENT
        } else JcrFolder(parent)
    }

    @Throws(WorkspaceException::class)
    override fun setParent(value: Folder) {
        val id = value.id ?: throw UnsupportedOperationException("Parent ID is null.")
        this.parentId = id
    }

    @Throws(WorkspaceException::class)
    override fun getParentId(): String {
        try {
            return this.node.parent.identifier
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

    }

    @Throws(WorkspaceException::class)
    override fun setParentId(value: String) {
        try {
            val path = this.session
                    .getNodeByIdentifier(value)
                    .path + "/"
            val destination = URI
                    .create(path)
                    .resolve(this.node.name)
                    .path
            this.move(destination)
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
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
        val ROOT_PATH = "/content"
    }
}
