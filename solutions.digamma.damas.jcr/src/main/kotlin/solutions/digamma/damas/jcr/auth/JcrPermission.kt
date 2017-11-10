package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.auth.Privilege
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.File
import solutions.digamma.damas.user.Subject
import java.util.Calendar
import java.util.EnumSet
import javax.jcr.Node

/**
 * @author Ahmad Shahwan
 */
class JcrPermission private constructor(
        val node: Node,
        val subject: String
): Permission {

    private var privileges: EnumSet<Privilege> =
            EnumSet.noneOf(Privilege::class.java)

    override fun getPrivileges() = this.privileges

    override fun setPrivileges(value: EnumSet<Privilege>) {
        this.privileges = value
    }


    override fun getSubject(): Subject? {
        return null
    }

    override fun setSubject(value: Subject) {

    }

    override fun getObject(): File? {
        return null
    }

    override fun setObject(value: File) {

    }

    @Throws(WorkspaceException::class)
    override fun getCreatedBy(): String? {
        return null
    }

    @Throws(WorkspaceException::class)
    override fun getCreationDate(): Calendar? {
        return null
    }

    @Throws(WorkspaceException::class)
    override fun getId(): String? {
        return null
    }

    @Throws(WorkspaceException::class)
    override fun getModifiedBy(): String? {
        return null
    }

    @Throws(WorkspaceException::class)
    override fun getModificationDate(): Calendar? {
        return null
    }
}
