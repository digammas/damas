package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.login.Token
import solutions.digamma.damas.common.MisuseException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.PathFinder
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.session.SessionConsumer
import java.nio.file.Paths
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * @author Ahmad Shahwan
 */
internal interface JcrPathFinder<T : Entity> : SessionConsumer, PathFinder<T> {

    @Throws(WorkspaceException::class)
    override fun find(token: Token, path: String): T {
        return Exceptions.wrap(openSession(token)) {
            var p = Paths.get(path).normalize().toString()
            if (p.startsWith("../")) {
                throw MisuseException("Invalid relative path.")
            }
            p = if (p == "") "." else p
            this.find(it.getSession(), p)
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
