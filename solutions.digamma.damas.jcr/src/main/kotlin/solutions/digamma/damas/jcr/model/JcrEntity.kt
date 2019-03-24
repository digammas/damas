package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.jcr.common.Exceptions
import java.time.ZonedDateTime
import java.util.Arrays
import java.util.Collections
import java.util.GregorianCalendar
import java.util.stream.Collectors
import javax.jcr.Node
import javax.jcr.NodeIterator
import javax.jcr.PathNotFoundException
import javax.jcr.Property
import javax.jcr.RepositoryException
import javax.jcr.Value
import javax.jcr.query.Query

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
     * @param name      property name
     * @return          property value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun getString(name: String): String? = Exceptions.wrap {
        this.getProperty(name)?.string
    }

    /**
     * Set or remove a string property.
     *
     * If `value` is not null property will be updated or created with this
     * value. Otherwise (`value` is null), property will be removed if it
     * exists.
     *
     * @param name      property name
     * @param value     property new value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun setString(name: String, value: String?) {
        Exceptions.wrap { this.node.setProperty(name, value) }
    }

    /**
     * List of all string values of a property.
     *
     * @param name      property name
     * @return          property value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun getStrings(name: String): List<String>? = Exceptions.wrap {
        this.getProperty(name)?.values?.map { it.string }?.toList()
    }

    /**
     * Set the list of string values of a property.
     *
     * If list is null, property will be removed. Otherwise a property will be
     * created if it didn't exist.
     *
     * @param name property name
     * @param values property string values
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun setStrings(name: String, values: List<String>?) {
        Exceptions.wrap {
            this.node.setProperty(name, values?.toTypedArray())
        }
    }

    /**
     * Date property.
     *
     * @param name      property name
     * @return          property value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun getDate(name: String): ZonedDateTime? = Exceptions.wrap {
        val cal = this.getProperty(name)?.date
        cal?.toInstant()?.atZone(cal.timeZone.toZoneId())
    }

    /**
     * Set or remove a date property.
     *
     * If `value` is not null property will be updated or created with
     * this value. Otherwise (`value` is null), property will be removed
     * if it exists.
     *
     * @param name      property name
     * @param value     property new value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun setDate(name: String, value: ZonedDateTime?): Unit = Exceptions.wrap {
        this.node.setProperty(name, value?.let { GregorianCalendar.from(it) })
    }

    /**
     * Integer property.
     *
     * @param name      property name
     * @return          property value
     * @throws WorkspaceException
     */
    
    @Throws(WorkspaceException::class)
    fun getLong(name: String): Long? = Exceptions.wrap {
        this.getProperty(name)?.long
    }

    /**
     * Set or remove integer property.
     *
     * If `value` is not null property will be updated or created with this
     * value. Otherwise (`value` is null), property will be removed if it
     * exists.
     *
     * @param name      property name
     * @param value     property new value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun setLong(name: String, value: Long?) {
        Exceptions.wrap {
            when (value) {
                null -> this.getProperty(name)?.remove()
                else -> this.node.setProperty(name, value)
            }
        }
    }

    /**
     * Boolean property.
     *
     * @param name      property name
     * @return          property value
     * @throws WorkspaceException
     */

    @Throws(WorkspaceException::class)
    fun getBoolean(name: String): Boolean? = Exceptions.wrap {
        this.getProperty(name)?.boolean
    }

    /**
     * Set or remove boolean property.
     *
     * If `value` is not null property will be updated or created with this
     * value. Otherwise (`value` is null), property will be removed if it
     * exists.
     *
     * @param name      property name
     * @param value     property new value
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun setBoolean(name: String, value: Boolean?) {
        Exceptions.wrap {
            when (value) {
                null -> this.getProperty(name)?.remove()
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

    private fun getProperty(name: String): Property? = try {
        this.node.getProperty(name)
    } catch (_: PathNotFoundException) { null }

    companion object {

        const val SQL2_SELECT_CHILDREN =
                "SELECT * FROM [%s] AS c WHERE ISCHILDNODE(c, %s)"
    }
}
