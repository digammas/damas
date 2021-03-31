package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.common.ResultPage
import solutions.digamma.damas.jcr.session.JcrSessionConsumer
import solutions.digamma.damas.logging.Logged
import solutions.digamma.damas.search.Filter
import solutions.digamma.damas.search.Page
import solutions.digamma.damas.search.SearchEngine
import javax.jcr.Node
import javax.jcr.Property
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
                this.doFind(offset, size, filter)
        }
    }

    /**
     * Perform search.
     *
     * @param offset
     * @param size
     * @param filter
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    fun doFind(
            offset: Int,
            size: Int,
            filter: Filter?): Page<T> {
        val sqlQuery = this.buildQuery(filter)
        return this.query(this.session, sqlQuery, offset, size)
    }

    fun getNodePrimaryType(): String

    fun getDefaultRootPath(): String

    fun fromNode(node: Node): T

    fun buildQuery(filter: Filter?): String {
        val sb = StringBuilder()
        sb.append("""
           SELECT * FROM [${this.getNodePrimaryType()}] AS node
           WHERE ISDESCENDANTNODE(node, '${this.getDefaultRootPath()}')
       """)
        this.appendClauses(sb, filter)
        sb.append("ORDER BY node.[${Property.JCR_CREATED}] ")
        return sb.toString()
    }

    fun appendClauses(sb: StringBuilder, filter: Filter?) {
        if (filter == null) {
            return
        }
        if (filter.namePattern != null) {
            sb.append(this.buildNameClause(filter.namePattern))
        }
        if (filter.scopeId != null) {
            sb.append(this.buildScopeClause(filter))
        }
    }

    fun buildNameClause(namePattern: String): String {
        val pattern = namePattern
                .replace("%", "%%")
                .replace('*', '%')
        return "AND node.[${Property.JCR_NAME}] LIKE '$pattern' "
    }

    fun buildScopeClause(filter: Filter): String {
        val predicate =
                if (filter.isRecursive) "ISDESCENDANTNODE"
                else "ISCHILDNODE"
        val path = this.session.getNodeByIdentifier(filter.scopeId).path
        return "AND $predicate (node, '$path') "
    }

    fun query(
            session: Session,
            sql2: String,
            offset: Int,
            size: Int): Page<T> {
        val result = session
                .workspace
                .queryManager
                .createQuery(sql2, Query.JCR_SQL2)
                .execute()
                .nodes
        val list = result
            .asSequence()
            .filterIsInstance<Node>()
            .drop(offset)
            .take(size)
            .map(::fromNode)
            .toList()
        return ResultPage(list, result.size.toInt())
    }

    companion object {

        /**
         * Default result page size.
         */
        const val DEFAULT_PAGE_SIZE = 30
    }
}
