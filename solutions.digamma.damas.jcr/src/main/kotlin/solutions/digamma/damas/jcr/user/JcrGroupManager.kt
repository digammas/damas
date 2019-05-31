package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.search.Page
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.search.Filter
import solutions.digamma.damas.user.Group
import solutions.digamma.damas.user.GroupManager
import java.lang.StringBuilder
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
    override fun find(session: Session, offset: Int, size: Int, filter: Filter?):
            Page<Group> {
        val sql2 = this.buildQuery(filter)
        return this.query(session, sql2, offset, size, JcrGroup.Companion::of)
    }

    private fun buildQuery(filter: Filter?): String {
        val sb = StringBuilder()
        sb.append("""
           SELECT * FROM [${TypeNamespace.GROUP}] AS group
           WHERE ISDESCENDANTNODE(group, '${JcrSubject.ROOT_PATH}')
       """)
        this.appendClauses(sb, filter)
        sb.append("ORDER BY group.[${Property.JCR_CREATED}] ")
        return sb.toString()
    }

    private fun appendClauses(sb: StringBuilder, filter: Filter?) {
        if (filter == null) {
            return
        }
        if (filter.namePattern != null) {
            val pattern = filter.namePattern
                    .replace("%", "%%")
                    .replace('*', '%')
            sb.append("AND group.[${Property.JCR_NAME}] LIKE '$pattern' ")
        }
    }
}