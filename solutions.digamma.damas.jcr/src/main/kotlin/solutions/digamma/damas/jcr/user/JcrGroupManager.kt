package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.Group
import solutions.digamma.damas.user.GroupManager
import javax.inject.Singleton
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * JCR implementation of group manager.
 *
 */
@Singleton
internal open class JcrGroupManager: JcrCrudManager<Group>(),
        JcrSearchEngine<Group>,
        GroupManager {

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun create(session: Session, pattern: Group) =
        JcrGroup.from(session, pattern.name).also { it.update(pattern) }

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun update(session: Session, id: String, pattern: Group) =
        retrieve(session, id).also { it.update(pattern) }

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun retrieve(session: Session, id: String) =
        JcrGroup.of(session.getNode("${JcrSubject.ROOT_PATH}/$id"))

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun delete(session: Session, id: String) =
        retrieve(session, id).remove()

    override fun getNodePrimaryType() = TypeNamespace.GROUP

    override fun getDefaultRootPath() = JcrSubject.ROOT_PATH

    override fun fromNode(node: Node) = JcrGroup.of(node)
}