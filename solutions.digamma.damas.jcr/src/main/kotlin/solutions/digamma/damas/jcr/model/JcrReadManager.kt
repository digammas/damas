package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.login.Token
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.entity.EntityManager
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.logging.Logged
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * Abstract read manager.
 *
 * @author Ahmad Shahwan
 */
internal abstract class JcrReadManager<T: Entity> : JcrManager(),
        EntityManager<T> {

    @Logged
    @Throws(WorkspaceException::class)
    override fun retrieve(id: String): T = Exceptions.check {
        this.retrieve(this.getSession(), id)
    }

    /**
     * Perform retrieval.
     *
     * @param session
     * @param id
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun retrieve(session: Session, id: String): T
}
