package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.jcr.common.Exceptions
import java.util.Arrays
import java.util.Calendar
import javax.jcr.Node
import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query
import kotlin.streams.toList

/**
 * Entity implementation, with underling JCR node.
 *
 * @author Ahmad Shahwan
 */
internal interface JcrEntity : Entity {

    /**
     * Underling JCR node.
     *
     * @return
     */
    val node: Node

    
    @Throws(WorkspaceException::class)
    override fun getId(): String = Exceptions.wrap { this.node.identifier }

    /**
     * String property.
     *
     * @param name Property name
     * @return Property value
     * @throws WorkspaceException
     */
    
    @Throws(WorkspaceException::class)
    fun getString(name: String): String = Exceptions.wrap {
        this.node.getProperty(name).string
    }

    /**
     * Set or remove a string property.
     *
     * If `value` is not null property will be updated or created with this
     * value. Otherwise (`value` is null), property will be removed if it
     * exists.
     *
     * @param name Property name
     * @param value Property new value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun setString(name: String, value: String) {
        Exceptions.wrap { this.node.setProperty(name, value) }
    }

    /**
     * Llist of all string values of a property.
     *
     * @param name                  Property name
     * @return
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun getStrings(name: String): List<String> = Exceptions.wrap<List<String>> {
        Arrays.stream(this.node.getProperty(name).values)
                .map { it.string }
                .toList()
    }


    /**
     * Date property.
     *
     * @param name Property name
     * @return Property value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun getDate(name: String): Calendar = Exceptions.wrap {
        this.node.getProperty(name).date
    }

    /**
     * Set or remove a date property.
     *
     * If `value` is not null property will be updated or created with
     * this value. Otherwise (`value` is null), property will be removed
     * if it exists.
     *
     * @param name Property name
     * @param value Property new value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun setDate(name: String, value: Calendar) {
        Exceptions.wrap { this.node.setProperty(name, value) }
    }

    /**
     * Integer property.
     *
     * @param name Property name
     * @return Property value
     * @throws WorkspaceException
     */
    
    @Throws(WorkspaceException::class)
    fun getLong(name: String): Long = Exceptions.wrap {
        this.node.getProperty(name).long
    }

    /**
     * Set or remove integer property.
     *
     * If `value` is not null property will be updated or created with this
     * value. Otherwise (`value` is null), property will be removed if it
     * exists.
     *
     * @param name Property name
     * @param value Property new value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun setLong(name: String, value: Long?) {
        Exceptions.wrap {
            when (value) {
                null -> this.node.getProperty(name).remove()
                else -> this.node.setProperty(name, value)
            }
        }
    }

    /**
     * Retrieve an iterator over child nodes of a certain type.
     *
     * @param type
     * @return
     * @throws RepositoryException
     */
    @Throws(RepositoryException::class)
    fun getChildNodes(type: String): NodeIterator {
        val path = this.node.path
        val sql2 = SQL2_SELECT_CHILDREN.format(type, path)
        val manager = this.node
                .session.workspace.queryManager
        val query = manager.createQuery(sql2, Query.JCR_SQL2)
        val result = query.execute()
        return result.nodes
    }

    companion object {

        val SQL2_SELECT_CHILDREN =
                "SELECT * FROM [%s] AS c WHERE ISCHILDNODE(c, %s)"
    }
}
