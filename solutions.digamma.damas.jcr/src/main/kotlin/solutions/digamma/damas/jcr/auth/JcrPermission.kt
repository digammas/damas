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
import java.util.EnumSet
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.security.AccessControlEntry
import javax.jcr.security.AccessControlList
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
        readPrivileges(this.acl ?: getAppliedEntries(this.node, this.subject))
    }

    @Throws(WorkspaceException::class)
    override fun setAccessRights(value: EnumSet<AccessRight>?) {
        if (value == null) return
        Exceptions.wrap {
            writePrivileges(this.node, this.subject, value)
        }
    }

    override fun getSubjectId(): String {
        return this.subject
    }

    @Throws(WorkspaceException::class)
    override fun getObject(): File {
        return JcrFile.of(this.node)
    }

    @Throws(WorkspaceException::class)
    fun remove() {
        Exceptions.wrap {
            writePrivileges(this.node, this.subject, null)
        }
    }

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
        internal fun getAppliedPolicy(node: Node):
                AccessControlList? {
            node.session.accessControlManager.getPolicies(node.path).forEach {
                if (it is AccessControlList) return it
            }
            return null
        }

        /**
         * Retrieve access control list applied at a given path.
         * If no ACL is applied return a new one.
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
            } else {
                val itr = node.session.accessControlManager
                        .getApplicablePolicies(node.path)
                while (itr.hasNext()) {
                    val policy = itr.nextAccessControlPolicy()
                    if (policy is AccessControlList) {
                        return policy
                    }
                }
            }
            throw UnsupportedActionException("ACL unsupported at ${node.path}.")
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
        internal fun readPrivileges(entries: List<AccessControlEntry>):
                EnumSet<AccessRight> {
            val accessRights = EnumSet.noneOf(AccessRight::class.java)
            for (entry in entries) {
                if (entry.privileges.any { it.name == Privilege.JCR_READ }) {
                    accessRights.add(READ)
                }
                if (entry.privileges.any { it.name == Privilege.JCR_WRITE }) {
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
                node: Node, subject: String, value: EnumSet<AccessRight>?) {
            val entries = getAppliedEntries(node, subject)
            if (value != readPrivileges(entries)) {
                val policy = getApplicablePolicy(node)
                /* Remove all old entries */
                entries.forEach({ policy.removeAccessControlEntry(it) })
                /* If value null, stop here */
                if (value == null) return
                val names: MutableList<String> = ArrayList(value.size)
                for (right in value) {
                    when (value) {
                        READ -> names.add(Privilege.JCR_READ)
                        WRITE -> names.add(Privilege.JCR_WRITE)
                    }
                }
                val privileges = names.map {
                    node.session.accessControlManager.privilegeFromName(it)
                }.toTypedArray()
                val principal = NamedPrincipal(subject)
                policy.addAccessControlEntry(principal, privileges)
            }
        }
    }
}
