package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.jcr.common.Exceptions
import java.time.ZonedDateTime
import java.util.GregorianCalendar
import javax.jcr.Node
import javax.jcr.NodeIterator
import javax.jcr.PathNotFoundException
import javax.jcr.Property
import javax.jcr.RepositoryException
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

    override fun getId(): String = Exceptions.uncheck { this.node.identifier }

    /**
     * String property.
     *
     * @param name      property name
     * @return          property value
     * @throws WorkspaceException
     */
    fun getString(name: String): String? = Exceptions.uncheck {
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
    fun setString(name: String, value: String?) {
        Exceptions.uncheck { this.node.setProperty(name, value) }
    }

    /**
     * List of all string values of a property.
     *
     * @param name      property name
     * @return          property value
     * @throws WorkspaceException
     */
    fun getStrings(name: String): List<String>? = Exceptions.uncheck {
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
    fun setStrings(name: String, values: List<String>?) {
        Exceptions.uncheck {
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
    fun getDate(name: String): ZonedDateTime? = Exceptions.uncheck {
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
    fun setDate(name: String, value: ZonedDateTime?) {
        Exceptions.uncheck {
            this.node.setProperty(name, value?.let {
                GregorianCalendar.from(it)
            })
        }
    }

    /**
     * Integer property.
     *
     * @param name      property name
     * @return          property value
     * @throws WorkspaceException
     */
    fun getLong(name: String): Long? = Exceptions.uncheck {
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
    fun setLong(name: String, value: Long?) {
        Exceptions.uncheck {
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
    fun getBoolean(name: String): Boolean? = Exceptions.uncheck {
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
     */
    fun setBoolean(name: String, value: Boolean?) {
        Exceptions.uncheck {
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

    private fun getProperty(name: String): Property? {
        return if (this.node.hasProperty(name)) {
            this.node.getProperty(name)
        } else null
    }

    companion object {

        const val SQL2_SELECT_CHILDREN =
                "SELECT * FROM [%s] AS c WHERE ISCHILDNODE(c, '%s')"
    }
}
