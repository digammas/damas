package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.login.Token
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.CrudManager
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.logging.Logged
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * JCR CRUD manager.
 *
 * @author Ahmad Shahwan
 */
internal abstract class JcrCrudManager<T: Entity>: JcrReadManager<T>(), CrudManager<T> {

    @Logged
    @Throws(WorkspaceException::class)
    override fun create(token: Token, entity: T): T =
            Exceptions.wrap(openSession(token)) {
        this.create(it.getSession(), entity)
    }


    @Logged
    @Throws(WorkspaceException::class)
    override fun update(token: Token, id: String, entity: T): T =
            Exceptions.wrap(openSession(token)) {
        this.update(it.getSession(), id, entity)
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun delete(token: Token, id: String): Unit =
            Exceptions.wrap(openSession(token)) {
        this.delete(it.getSession(), id)
    }

    /**
     * Perform creation.
     *
     * @param session
     * @param pattern
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun create(session: Session, pattern: T): T

    /**
     * Perform update.
     *
     * @param session
     * @param id
     * @param pattern
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun update(session: Session, id: String, pattern: T): T

    /**
     * Perform deletion.
     *
     * @param session
     * @param id
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun delete(session: Session, id: String)
}
