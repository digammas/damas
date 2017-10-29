package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.Group
import javax.jcr.ItemExistsException
import javax.jcr.Node
import javax.jcr.Session

internal class JcrGroup
@Throws(WorkspaceException::class)
private constructor(node: Node) : JcrSubject(node), Group {

    @Throws(WorkspaceException::class)
    override fun getName(): String = Exceptions.wrap { this.node.name }

    @Throws(WorkspaceException::class)
    override fun setName(value: String?) {
        if (value != null) Exceptions.wrap {
            val destination = "${this.node.path}/$value"
            try {
                this.node.session.move(this.node.path, destination)
            } catch(e: ItemExistsException) {
                throw SubjectExistsException(value, e)
            }
        }
    }

    @Throws(WorkspaceException::class)
    fun update(other: Group) {
        other.name?.let { this.name = it }
    }

    companion object {

        @Throws(WorkspaceException::class)
        fun of(node: Node) = JcrGroup(node)

        @Throws(WorkspaceException::class)
        fun from(session: Session, name: String) = Exceptions.wrap {
            val root = session.getNode(JcrSubject.ROOT_PATH)
            try {
                of(root.addNode(name, TypeNamespace.GROUP))
            } catch(e: ItemExistsException) {
                throw SubjectExistsException(name, e)
            }
        }
    }
}

