package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.CrudManager
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.logging.Logged
import javax.jcr.RepositoryException

/**
 * JCR CRUD manager.
 *
 * @author Ahmad Shahwan
 */
internal abstract class JcrCrudManager<T: Entity>: JcrReadManager<T>(), CrudManager<T> {

    @Logged
    @Throws(WorkspaceException::class)
    override fun create(entity: T): T = Exceptions.check {
        this.doCreate(entity)
    }


    @Logged
    @Throws(WorkspaceException::class)
    override fun update(id: String, entity: T): T = Exceptions.check {
        this.doUpdate(id, entity)
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun delete(id: String): Unit = Exceptions.check {
        this.doDelete(id)
    }

    /**
     * Perform creation.
     *
     * @param pattern
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun doCreate(pattern: T): T

    /**
     * Perform update.
     *
     * @param id
     * @param pattern
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun doUpdate(id: String, pattern: T): T

    /**
     * Perform deletion.
     *
     * @param id
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun doDelete(id: String)
}
