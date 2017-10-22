package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.entity.CrudManager
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.inspection.NotNull
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.session.SessionWrapper
import solutions.digamma.damas.logging.Logged

import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * JCR CRUD manager.
 *
 * @author Ahmad Shahwan
 */
abstract class JcrCrudManager<T : Entity> : JcrReadManager<T>(), CrudManager<T> {

    @Logged
    @Throws(WorkspaceException::class)
    override fun create(token: Token, entity: T): T {
        try {
            openSession(token).use { session -> return this.create(session.getSession(), entity) }
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun update(token: Token, id: String, entity: T): T {
        try {
            openSession(token).use { session -> return this.update(session.getSession(), id, entity) }
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun delete(token: Token, id: String) {
        try {
            openSession(token).use { session -> this.delete(session.getSession(), id) }
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

    }

    /**
     * Perform creation.
     *
     * @param session
     * @param entity
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun create(
            session: Session, entity: T): T

    /**
     * Perform update.
     *
     * @param session
     * @param id
     * @param entity
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun update(
            session: Session,
            id: String,
            entity: T): T

    /**
     * Perform deletion.
     *
     * @param session
     * @param id
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    protected abstract fun delete(
            session: Session, id: String)
}
