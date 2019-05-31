package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.search.Page
import solutions.digamma.damas.search.SearchEngine
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.common.ResultPage
import solutions.digamma.damas.jcr.session.JcrSessionConsumer
import solutions.digamma.damas.logging.Logged
import solutions.digamma.damas.search.Filter
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.query.Query

/**
 * @author Ahmad Shahwan
 */
internal interface JcrSearchEngine<T : Entity>
    : JcrSessionConsumer, SearchEngine<T, Filter> {

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
    override fun find(offset: Int, size: Int, filter: Filter?): Page<T> {
        return Exceptions.check {
                this.find(this.getSession(), offset, size, filter)
        }
    }


    /**
     * Perform search.
     *
     * @param session
     * @param offset
     * @param size
     * @param filter
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    fun find(
            session: Session,
            offset: Int,
            size: Int,
            filter: Filter?): Page<T>

    fun query(
            session: Session,
            sql2: String,
            offset: Int,
            size: Int,
            of: (node: Node) -> T): Page<T> {
        val result = session
                .workspace
                .queryManager
                .createQuery(sql2, Query.JCR_SQL2)
                .execute()
                .nodes
        result.skip(offset.toLong())
        var count = 0
        val list: MutableList<T> = ArrayList(size)
        while (result.hasNext() && count++ < size) {
            list.add(of(result.nextNode()))
        }
        return ResultPage(list, result.size.toInt())
    }

    companion object {

        /**
         * Default result page size.
         */
        const val DEFAULT_PAGE_SIZE = 30
    }
}
