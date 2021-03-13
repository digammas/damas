package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.entity.EntityManager
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.logging.Logged
import javax.jcr.RepositoryException

/**
 * Abstract read manager.
 *
 * @author Ahmad Shahwan
 */
internal abstract class JcrReadManager<T: Entity> : JcrManager(),
        EntityManager<T> {

    /**
     * Retrieve an existing entity.
     *
     * This method converts a thrown exception into a [WorkspaceException].
     */
    @Logged
    @Throws(WorkspaceException::class)
    override fun retrieve(id: String): T = Exceptions.check {
        this.doRetrieve(id)
    }

    /**
     * Perform retrieval.
     *
     * @param id
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun doRetrieve(id: String): T
}
