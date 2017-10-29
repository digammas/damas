package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.common.MisuseException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.PathFinder
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.session.SessionUser
import java.nio.file.Paths
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * @author Ahmad Shahwan
 */
internal interface JcrPathFinder<T : Entity> : SessionUser, PathFinder<T> {

    @Throws(WorkspaceException::class)
    override fun find(token: Token, path: String): T {
        var path = path
        return Exceptions.wrap(openSession(token)) {
            path = Paths.get(path).normalize().toString()
            if (path.startsWith("../")) {
                throw MisuseException("Invalid relative path.")
            }
            path = if (path == "") "." else path
            this.find(it.getSession(), path)
        }
    }

    /**
     * Perform path look-up.
     *
     * @param session
     * @param path
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    fun find(session: Session, path: String): T
}
