package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Page
import solutions.digamma.damas.jcr.common.ResultPage
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import solutions.digamma.damas.user.User
import solutions.digamma.damas.user.UserManager
import java.util.Collections
import javax.inject.Singleton
import javax.jcr.RepositoryException
import javax.jcr.Session

@Singleton
class JcrUserManager : JcrCrudManager<User>(),
        JcrSearchEngine<User>,
        UserManager {

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun create(session: Session, pattern: User) =
            JcrUser.from(session, pattern.login).also { it.update(pattern) }

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun update(session: Session, id: String, pattern: User) =
            retrieve(session, id).also { it.update(pattern) }

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun retrieve(session: Session, id: String) =
            JcrUser.of(session.getNodeByIdentifier(id))

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun delete(session: Session, id: String) =
            retrieve(session, id).remove()

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun find(session: Session, offset: Int, size: Int, query: Any?):
            Page<User> = ResultPage(Collections.emptyList())
}