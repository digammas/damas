package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.auth.AccessRight.READ
import solutions.digamma.damas.auth.AccessRight.WRITE
import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.common.UnsupportedActionException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.File
import solutions.digamma.damas.jaas.NamedPrincipal
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.content.JcrFile
import solutions.digamma.damas.jcr.sys.SystemRole
import java.util.Arrays
import java.util.EnumSet
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.security.AccessControlEntry
import javax.jcr.security.AccessControlList
import javax.jcr.security.AccessControlManager
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
        readPrivileges(
                this.node.session.accessControlManager,
                this.acl ?: getAppliedEntries(this.node, this.subject)
        )
    }

    @Throws(WorkspaceException::class)
    override fun setAccessRights(value: EnumSet<AccessRight>?) {
        if (value == null) return
        if (isProtected()) {
            throw PermissionProtectedException(subjectId, node.path)
        }
        Exceptions.wrap {
            writePrivileges(this.node, this.subject, value)
        }
    }

    override fun getSubjectId(): String = this.subject

    @Throws(WorkspaceException::class)
    override fun getObject(): File = JcrFile.of(this.node)

    @Throws(WorkspaceException::class)
    fun remove() {
        Exceptions.wrap {
            writePrivileges(this.node, this.subject, AccessRight.none())
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
            /* Retrieve object node, a file. */
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
            getAppliedPolicy(node)?.accessControlEntries?.groupBy {
                it.principal.name
            }?.map {
                JcrPermission(node, it.key, it.value)
            } ?: emptyList()
        }

        /**
         * Retrieve access control list applied at a given path.
         * If no ACL is applied return null.
         *
         * @param node JCR node
         * @return ACL policy applied at path, if any, `null` otherwise
         */
        @Throws(RepositoryException::class)
        private fun getAppliedPolicy(node: Node):
                AccessControlList? {
            node.session.accessControlManager.getPolicies(node.path).forEach {
                if (it is AccessControlList) return it
            }
            return null
        }

        /**
         * Retrieve access control list applied at a given path.
         * If no ACL is applied return a new one, after having added default
         * access rights.
         *
         * @param node JCR node
         * @return ACL policy applied at path, if any, new one otherwise
         * @throws UnsupportedActionException if repository doesn't support ACL
         */
        @Throws(RepositoryException::class, UnsupportedActionException::class)
        private fun getApplicablePolicy(node: Node):
                AccessControlList {
            val acl = getAppliedPolicy(node)
            if (acl != null) {
                return acl
            }
            val path = node.path
            val session = node.session
            val itr = session.accessControlManager
                    .getApplicablePolicies(path)
            while (itr.hasNext()) {
                val policy = itr.nextAccessControlPolicy()
                if (policy is AccessControlList) {
                    preparePolicy(session, policy)
                    return policy
                }
            }
            throw UnsupportedActionException("ACL unsupported at $path.")
        }

        @Throws(RepositoryException::class)
        private fun getAppliedEntries(node: Node, subject: String):
                List<AccessControlEntry> {
            val list = getAppliedPolicy(node)?.accessControlEntries?.filter {
                it.principal.name == subject
            }
            return list ?: emptyList()
        }

        @Throws(RepositoryException::class)
        private fun readPrivileges(
                acm: AccessControlManager,
                entries: List<AccessControlEntry>): EnumSet<AccessRight> {
            val accessRights = AccessRight.none()
            val jcrRead = acm.privilegeFromName(Privilege.JCR_READ)
            val jcrWrite = acm.privilegeFromName(Privilege.JCR_WRITE)
            for (entry in entries) {
                if (entry.privileges.any { it == jcrRead }) {
                    accessRights.add(READ)
                }
                if (entry.privileges.any { it == jcrWrite }) {
                    accessRights.add(WRITE)
                }
            }
            return accessRights
        }

        /**
         * Rewrite privileges of a subject at a given node.
         *
         * @param node      JCR node
         * @param subject   subject ID
         * @param value     set of privileges, if null, delete all privileges
         */
        @Throws(RepositoryException::class)
        private fun writePrivileges(
                node: Node, subject: String, value: EnumSet<AccessRight>) {
            val policy = getApplicablePolicy(node)
            val acm = node.session.accessControlManager
            /* Remove all old entries of this subject */
            policy.accessControlEntries.filter {
                it.principal.name == subject
            }.forEach {
                policy.removeAccessControlEntry(it)
            }
            /* If value null or empty, stop here */
            if (value.isEmpty()) return
            val names: MutableList<String> = ArrayList(value.size)
            for (right in value) {
                when (right) {
                    READ -> names.add(Privilege.JCR_READ)
                    WRITE -> names.add(Privilege.JCR_WRITE)
                    else -> {}
                }
            }
            val privileges = names.map {
                acm.privilegeFromName(it)
            }.toTypedArray()
            val principal = NamedPrincipal(subject)
            policy.addAccessControlEntry(principal, privileges)
            acm.setPolicy(node.path, policy)
        }

        /**
         * Add default access right to a policy.
         * Those are admin rights (full access) superuser rights (read/write
         * access and readonly user rights.
         *
         * @param session user JCR session
         * @param policy policy to prepare
         */
        private fun preparePolicy(
                session: Session,
                policy: AccessControlList) {
            val acm = session.accessControlManager
            policy.addAccessControlEntry(SystemRole.ADMIN, arrayOf(
                    acm.privilegeFromName(Privilege.JCR_ALL)
            ))
            policy.addAccessControlEntry(SystemRole.READWRITE,   arrayOf(
                    acm.privilegeFromName(Privilege.JCR_READ),
                    acm.privilegeFromName(Privilege.JCR_WRITE)
            ))
            policy.addAccessControlEntry(SystemRole.READONLY, arrayOf(
                    acm.privilegeFromName(Privilege.JCR_READ)
            ))
        }
    }
}
