package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Page
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.Group
import solutions.digamma.damas.user.GroupManager
import javax.inject.Singleton
import javax.jcr.Property
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * JCR implementation of group manager.
 *
 */
@Singleton
internal class JcrGroupManager: JcrCrudManager<Group>(),
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

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun find(session: Session, offset: Int, size: Int, query: Any?):
            Page<Group> {
        val sql2 = """
           SELECT * FROM [${TypeNamespace.GROUP}] AS user
           WHERE ISDESCENDANTNODE(user, '${JcrSubject.ROOT_PATH}')
           ORDER BY user.[${Property.JCR_CREATED}]
        """
        return this.query(session, sql2, offset, size, JcrGroup.Companion::of)
    }
}