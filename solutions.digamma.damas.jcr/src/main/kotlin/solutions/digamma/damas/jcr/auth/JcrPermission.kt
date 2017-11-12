package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.auth.AccessRight.READ
import solutions.digamma.damas.auth.AccessRight.WRITE
import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.common.InternalStateException
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
 * @author Ahmad Shahwan
 */
class JcrPermission
@Throws(WorkspaceException::class)
private constructor(
        private val node: Node,
        private val subject: String
): Permission {

    @Throws(WorkspaceException::class)
    override fun getAccessRights() = Exceptions.wrap {
        readPrivileges(getAppliedEntries(this.node, this.subject))
    }

    override fun getId(): String {
        return "${node.identifier}$ID_SEPARATOR$subject"
    }

    override fun setAccessRights(value: EnumSet<AccessRight>?) {
        if (value == null) return
        Exceptions.wrap {
            writePrivileges(this.node, this.subject, value)
        }
    }

    override fun getSubjectId(): String {
        return this.subject
    }

    override fun getObject(): File {
        return JcrFile.of(this.node)
    }

    companion object {

        private const val ID_SEPARATOR = ":"

        @Throws(WorkspaceException::class)
        fun of(session: Session, id: String) = Exceptions.wrap {
            val ids = id.split(ID_SEPARATOR)
            ids.size == 2 || throw InternalStateException("Invalid ID")
            /* Retrieve object node, a file. */
            val node = session.getNodeByIdentifier(ids[0])
            JcrPermission(node, ids[2])
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
         * If no ACL is applied return a new one.
         *
         * @param node JCR node
         * @return ACL policy applied at path, if any, new one otherwise
         * @throws UnsupportedActionException if repository doesn't support ACL
         */
        @Throws(RepositoryException::class, UnsupportedActionException::class)
        private fun getApplicablePolicy(node: Node):
                AccessControlList {
            var policy = getAppliedPolicy(node)
            if (policy != null) {
                return policy
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
        private fun readPrivileges(entries: List<AccessControlEntry>):
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

        @Throws(RepositoryException::class)
        private fun writePrivileges(
                node: Node, subject: String, value: EnumSet<AccessRight>) {
            val entries = getAppliedEntries(node, subject)
            if (value != readPrivileges(entries)) {
                val policy = getApplicablePolicy(node)
                /* Remove all old entries */
                entries.forEach({ policy.removeAccessControlEntry(it) })
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
