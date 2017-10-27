package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.entity.Page
import solutions.digamma.damas.entity.SearchEngine
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.session.SessionUser
import solutions.digamma.damas.logging.Logged
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * @author Ahmad Shahwan
 */
interface JcrSearchEngine<T : Entity> : SessionUser, SearchEngine<T> {

    /**
     * The size convert returned result page when no size is specified.
     *
     * @return
     */
    val defaultPageSize: Int
        get() = DEFAULT_PAGE_SIZE

    @Throws(WorkspaceException::class)
    override fun find(token: Token): Page<T> {
        return this.find(token, 0, this.defaultPageSize, null)
    }

    @Throws(WorkspaceException::class)
    override fun find(
            token: Token, offset: Int, size: Int): Page<T> {
        return this.find(token, offset, size, null)
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun find(
            token: Token, offset: Int, size: Int, query: Any?): Page<T> {
        return Exceptions.wrap(openSession(token)) {
                this.find(it.getSession(), offset, size, query)
        }
    }


    /**
     * Perform search.
     *
     * @param session
     * @param offset
     * @param size
     * @param query
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    fun find(
            session: Session,
            offset: Int,
            size: Int,
            query: Any?): Page<T>

    companion object {

        /**
         * Default result page size.
         */
        val DEFAULT_PAGE_SIZE = 30
    }
}
