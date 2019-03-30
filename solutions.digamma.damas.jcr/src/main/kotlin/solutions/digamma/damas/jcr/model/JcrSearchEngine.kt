package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.entity.Page
import solutions.digamma.damas.entity.SearchEngine
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.session.JcrSessionConsumer
import solutions.digamma.damas.logging.Logged
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * @author Ahmad Shahwan
 */
internal interface JcrSearchEngine<T : Entity>
    : JcrSessionConsumer, SearchEngine<T> {

    /**
     * The size convert returned result page when no size is specified.
     *
     * @return
     */
    val defaultPageSize: Int
        get() = DEFAULT_PAGE_SIZE

    @Throws(WorkspaceException::class)
    override fun find(): Page<T> {
        return this.find(0, this.defaultPageSize, null)
    }

    @Throws(WorkspaceException::class)
    override fun find(offset: Int, size: Int): Page<T> {
        return this.find(offset, size, null)
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun find(offset: Int, size: Int, query: Any?): Page<T> {
        return Exceptions.check {
                this.find(this.getSession(), offset, size, query)
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
        const val DEFAULT_PAGE_SIZE = 30
    }
}
