package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.common.UnsupportedActionException
import solutions.digamma.damas.jaas.NamedPrincipal
import solutions.digamma.damas.jcr.sys.SystemRole
import java.util.EnumSet
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.security.AccessControlEntry
import javax.jcr.security.AccessControlList
import javax.jcr.security.Privilege

internal object Permissions {
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
    internal fun getAppliedEntries(node: Node, subject: String):
            List<AccessControlEntry> {
        val list = Permissions.getAppliedPolicy(node)?.accessControlEntries?.filter {
            it.principal.name == subject
        }
        return list ?: emptyList()
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
        policy.addAccessControlEntry(SystemRole.SHADOW, arrayOf(
                acm.privilegeFromName(Privilege.JCR_ALL)
        ))
    }

    /**
     * Rewrite privileges of a subject at a given node.
     *
     * @param node      JCR node
     * @param subject   subject ID
     * @param value     set of privileges, if null, delete all privileges
     */
    @Throws(RepositoryException::class)
    internal fun writePrivileges(
            node: Node, subject: String, value: EnumSet<AccessRight>) {
        val policy = Permissions.getApplicablePolicy(node)
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
                AccessRight.READ -> names.add(Privilege.JCR_READ)
                AccessRight.WRITE -> {
                    names.add(Privilege.JCR_READ)
                    names.add(Privilege.JCR_WRITE)
                }
                AccessRight.MAINTAIN -> names.add(Privilege.JCR_ALL)
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
     * Grant access rights to connected user on a given node.
     * This is usually only possible when the node is newly created (by the
     * connected user), and has no policies of its own yet.
     *
     * @param node the node on which access rights are granted
     * @param rights access rights to be granted
     */
    internal fun selfGrant(node: Node, rights: EnumSet<AccessRight>) {
        val subject = node.session.userID
        if (!SystemRole.values().map { it.name }.contains(subject)) {
            writePrivileges(node, node.session.userID, rights)
        }
    }
}

