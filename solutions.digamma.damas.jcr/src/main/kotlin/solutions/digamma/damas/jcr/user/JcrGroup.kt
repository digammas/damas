package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.Group
import javax.jcr.Node
import javax.jcr.Session

class JcrGroup
@Throws(WorkspaceException::class)
private constructor(node: Node) : JcrSubject(node), Group {

    @Throws(WorkspaceException::class)
    override fun getName(): String = Exceptions.wrap { this.node.name }

    @Throws(WorkspaceException::class)
    override fun setName(value: String?) {
        if (value != null) Exceptions.wrap {
            val destination = "${this.node.path}/$value"
            this.node.session.move(this.node.path, destination)
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
        fun from(session: Session, login: String) = Exceptions.wrap {
            val root = session.getNode(JcrSubject.ROOT_PATH)
            of(root.addNode(login, TypeNamespace.GROUP))
        }
    }
}

