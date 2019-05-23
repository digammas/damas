package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.ItemNamespace
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.Group
import javax.jcr.ItemExistsException
import javax.jcr.Node
import javax.jcr.Session

/**
 * User group with JCR backbone.
 *
 * Groups are identified by their names. Group names cannot be modified.
 *
 * @constructor private constructor with JCR node
 * @param node              JCR node
 * @throws WorkspaceException
 */
internal class JcrGroup
@Throws(WorkspaceException::class)
private constructor(node: Node) : JcrSubject(node), Group {

    override fun getId(): String = this.name

    override fun getName(): String =
        this.getString(ItemNamespace.ALIAS)!!

    override fun getLabel(): String? {
        return this.getString(ItemNamespace.LABEL)
    }

    override fun setLabel(value: String?) {
        if (value != null) {
            this.setString(ItemNamespace.LABEL, value)
        }
    }

    @Throws(WorkspaceException::class)
    fun update(other: Group) {
        other.label?.let { this.label = it }
    }

    companion object {

        @Throws(WorkspaceException::class)
        fun of(node: Node) = JcrGroup(node)

        @Throws(WorkspaceException::class)
        fun from(session: Session, name: String) = Exceptions.check {
            val node = session.getNode(JcrSubject.ROOT_PATH)
                    .addNode(name, TypeNamespace.GROUP)
                    .also { it.setProperty(ItemNamespace.ALIAS, name) }
            try {
                of(node)
            } catch(e: ItemExistsException) {
                throw SubjectExistsException(name, e)
            }
        }
    }
}

