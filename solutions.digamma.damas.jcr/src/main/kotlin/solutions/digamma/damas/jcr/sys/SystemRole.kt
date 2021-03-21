package solutions.digamma.damas.jcr.sys

import java.security.Principal

/**
 * System principals. Those are roles that run a JCR repository.
 *
 * Each of these principals gives its owners certain coarse grained permissions
 * that apply repository-wide. However, the owner must also have relevant access
 * rights on the node at hand in order to be able to perform a certain action.
 * Those access rights are managed by the authorization module.
 */
enum class SystemRole(private val principal: String): Principal {

    /**
     * Read-only role. This role gives its owner a read access repository-wide.
     * To actually be able to read content, users need to have the relevant
     * access right on the node being read.
     *
     * This role is used to restrict access for certain account (such as system
     * RO) to read-only.
     */
    READONLY("readonly"),

    /**
     * Read-write role. This role gives its owner a read-write access
     * repository-wide. To actually be able to read or write content, users need
     * to have the relevant access right on the node being read or modified.
     *
     * This role is assigned to all users to permit coarse-grained repository-
     * wide access. Fine-grained read/write access rights are still needed to
     * access content and are managed by the authentication module.
     */
    READWRITE("readwrite"),

    /**
     * Admin role. Besides read-write access, this role gives its owner an
     * administrator access repository-wide. Admin access allow for special
     * operations such as unlocking any lock without having the lock token.
     *
     * This role is assigned only to the admin user. Note that by itself it is
     * not enough to permit superuser access. The user still need to have full
     * access on the item being handled.
     */
    ADMIN("admin"),

    /**
     * System shadow role. This role is systematically granted full access
     * rights ([javax.jcr.security.Privilege.JCR_ALL]) to all newly created
     * access-controlled nodes. Non-access-controlled nodes inherit this
     * access control entry from their parents.
     *
     * Admin user is also assigned this role besides [ADMIN]. Both roles
     * combined give the admin user the superpower they need.
     *
     * This role is also assigned to system users (RO and SU). Combined with
     * either [READONLY] or [READWRITE] system users can accomplish their tasks
     * of reading or reading/writing content respectively.
     */
    SHADOW("dms:shadow");

    override fun getName(): String = this.principal
}
