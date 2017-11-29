package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.auth.AccessRight.READ
import solutions.digamma.damas.auth.AccessRight.WRITE
import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.File
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.content.JcrFile
import solutions.digamma.damas.jcr.sys.SystemRole
import java.util.Arrays
import java.util.EnumSet
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

    @Throws(WorkspaceException::class)
    override fun getAccessRights() = Exceptions.wrap {
        val acl = this.acl ?: Permissions.getAppliedEntries(this.node, this.subject)
        val acm = this.node.session.accessControlManager
        val jcrRead = acm.privilegeFromName(Privilege.JCR_READ)
        val jcrWrite = acm.privilegeFromName(Privilege.JCR_WRITE)
        val accessRights = AccessRight.none()!!
        for (entry in acl) {
            if (entry.privileges.any { it == jcrRead }) {
                accessRights.add(READ)
            }
            if (entry.privileges.any { it == jcrWrite }) {
                accessRights.add(WRITE)
            }
        }
        accessRights
    }

    @Throws(WorkspaceException::class)
    override fun setAccessRights(value: EnumSet<AccessRight>?) {
        if (value == null) return
        if (isProtected()) {
            throw PermissionProtectedException(subjectId, node.path)
        }
        Exceptions.wrap {
            Permissions.writePrivileges(this.node, this.subject, value)
        }
    }

    override fun getSubjectId(): String = this.subject

    @Throws(WorkspaceException::class)
    override fun getObject(): File = JcrFile.of(this.node)

    @Throws(WorkspaceException::class)
    fun remove() {
        if (isProtected()) {
            throw PermissionProtectedException(subjectId, node.path)
        }
        Exceptions.wrap {
            Permissions.writePrivileges(this.node, this.subject, AccessRight.none())
            this.node.session.save()
        }
    }

    /**
     * Whether this permission is protected. Protected permissions are
     * immutable.
     * All permissions applying to a system role, or the current user are
     * protected.
     * Current user is not allowed to modify its own permissions.
     */
    private fun isProtected(): Boolean = this.subject in Arrays.asList(
            SystemRole.ADMIN.name,
            SystemRole.READONLY.name,
            SystemRole.READWRITE.name,
            this.node.session.userID
    )

    companion object {

        @Throws(WorkspaceException::class)
        fun of(session: Session, fileId: String, subjectId: String) =
                Exceptions.wrap {
            /* Retrieve obqject node, a file. */
            val node = session.getNodeByIdentifier(fileId)
            JcrPermission(node, subjectId)
        }

        /**
         * Optimized way to retrieve all permissions applied at a given node.
         */
        @Throws(WorkspaceException::class)
        fun lisOf(session: Session, fileId: String):
                List<JcrPermission> = Exceptions.wrap {
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
