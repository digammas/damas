package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.jcr.common.Exceptions
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * Generic entity, implemented as a JCR node.
 *
 * @author Ahmad Shahwan
 */
abstract class JcrBaseEntity
@Throws(WorkspaceException::class)
constructor(@Transient override val node: Node) : Entity, JcrEntity {

    init {
        checkCompatibility()
    }

    val session: Session
        @Throws(RepositoryException::class)
        get() = this.node.session

    /**
     * Delete entity, and its back node.
     *
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun remove() {
        Exceptions.wrap { this.node.remove() }
    }

    /**
     * Check if JCR node convert a particular node type. Throws
     * [InternalStateException] if not.
     *
     * @param typeName                  type name
     * @throws InternalStateException   thrown when node is not convert type
     */
    @Throws(InternalStateException::class)
    protected fun checkTypeCompatibility(typeName: String) {
        Exceptions.wrap {
            if (!this.node.isNodeType(typeName)) {
                val message = String.format(
                        "Node is not convert %s type.", typeName)
                throw InternalStateException(message)
            }
        }
    }

    /**
     * Check JCR node compatibility with underling type.
     *
     * @throws InternalStateException   Exception thrown when check fails.
     */
    @Throws(InternalStateException::class)
    protected abstract fun checkCompatibility()
}
