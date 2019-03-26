package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.File
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.content.JcrFile
import solutions.digamma.damas.jcr.login.UserLoginModule
import solutions.digamma.damas.jcr.sys.SystemRole
import solutions.digamma.damas.jcr.sys.SystemSessions
import java.util.Arrays
import javax.jcr.Node
import javax.jcr.Session
import javax.jcr.security.AccessControlEntry
import javax.jcr.security.Privilege

/**
 * JCR implementation of a permission entry.
 *
 * @author Ahmad Shahwan
 */
internal class JcrPermission
@Throws(WorkspaceException::class)
private constructor(
        private val node: Node,
        private val subject: String,
        private val acl: List<AccessControlEntry>? = null
): Permission {

    override fun getAccessRights(): AccessRight = Exceptions.uncheck {
        val acl = this.acl ?: Permissions
                .getAppliedEntries(this.node, this.subject)
        val acm = this.node.session.accessControlManager
        val jcrRead = acm.privilegeFromName(Privilege.JCR_READ)
        val jcrWrite = acm.privilegeFromName(Privilege.JCR_WRITE)
        val jcrAll = acm.privilegeFromName(Privilege.JCR_ALL)
        if (acl.isEmpty()) AccessRight.NONE else acl.map {
            when {
                it.privileges.contains(jcrAll) -> AccessRight.MAINTAIN
                it.privileges.contains(jcrWrite) -> AccessRight.WRITE
                it.privileges.contains(jcrRead) -> AccessRight.READ
                else -> AccessRight.NONE
            }
        }.reduce(::maxOf)
    }

    override fun setAccessRights(value: AccessRight?) = Exceptions.uncheck {
        if (value != null) {
            if (isProtected()) {
                throw PermissionProtectedException(subjectId, node.path)
            }
            Permissions.writePrivileges(this.node, this.subject, value)
        }
    }

    override fun getSubjectId(): String = this.subject

    override fun getObject(): File =
            Exceptions.uncheck { JcrFile.of(this.node) }

    fun remove() = Exceptions.uncheck {
        if (isProtected()) {
            throw PermissionProtectedException(subjectId, node.path)
        }
        Permissions.writePrivileges(
                this.node, this.subject, AccessRight.NONE)
    }

    /**
     * Whether this permission is protected. Protected permissions are
     * immutable.
     * All permissions applying to a system role, or the current user are
     * protected.
     * Current user is not allowed to modify its own permissions.
     */
    private fun isProtected(): Boolean = this.subject in Arrays.asList(
            SystemSessions.RO_USERNAME,
            SystemSessions.SU_USERNAME,
            UserLoginModule.ADMIN_USERNAME,
            SystemRole.SHADOW.name,
            this.node.session.userID
    )

    companion object {

        @Throws(WorkspaceException::class)
        fun of(session: Session, fileId: String, subjectId: String) =
                Exceptions.check {
            /* Retrieve object node, a file. */
            val node = session.getNodeByIdentifier(fileId)
            JcrPermission(node, subjectId)
        }

        /**
         * Optimized way to retrieve all permissions applied at a given node.
         */
        @Throws(WorkspaceException::class)
        fun lisOf(session: Session, fileId: String):
                List<JcrPermission> = Exceptions.check {
            /* Retrieve object node, a file. */
            val node = session.getNodeByIdentifier(fileId)
            Permissions.getAppliedPolicy(node)?.accessControlEntries?.groupBy {
                it.principal.name
            }?.map {
                JcrPermission(node, it.key, it.value)
            } ?: emptyList()
        }
    }
}
