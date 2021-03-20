package solutions.digamma.damas.jcr.login

import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.auth.AbstractLoginModule
import solutions.digamma.damas.jcr.sys.SystemRole
import solutions.digamma.damas.jcr.sys.SystemSessions
import solutions.digamma.damas.jcr.user.JcrSubject
import solutions.digamma.damas.jcr.user.JcrUser
import java.security.Principal
import java.util.Arrays
import javax.jcr.PathNotFoundException
import javax.security.auth.login.AccountLockedException
import javax.security.auth.login.AccountNotFoundException
import javax.security.auth.login.CredentialNotFoundException
import javax.security.auth.login.FailedLoginException
import javax.security.auth.login.LoginException

/**
 * @author Ahmad Shahwan
 */
internal open class UserLoginModule : AbstractLoginModule() {

    @Throws(LoginException::class)
    override fun doLogin(): Boolean {
        /* If system sessions not yes set, bypass this login module. */
        val sys = UserLoginModule.system ?: return false
        this.login ?: throw CredentialNotFoundException("Missing login")
        this.password ?: throw CredentialNotFoundException("Missing password")
        val ro = sys.readonly
        val node = try {
            ro.getNode("${JcrSubject.ROOT_PATH}/$login")
        } catch (_: PathNotFoundException) {
            return loginAdmin() ||
                    throw AccountNotFoundException("No such login")
        }
        val user = try {
            JcrUser.of(node)
        } catch (_: InternalStateException) {
            return loginAdmin() ||
                    throw AccountNotFoundException("Account is not a user")
        }
        try {
            user.isEnabled || throw AccountLockedException("Account disabled")
            user.checkPassword(String(this.password)) ||
                    throw FailedLoginException("Invalid password")
            this.roles.addAll(user.memberships.map { Principal { it } })
            this.roles.add(SystemRole.READWRITE)
            return true
        } catch (e: WorkspaceException) {
            throw LoginException("Authentication error")
        }
    }

    private fun loginAdmin(): Boolean {
        if (ADMIN_USERNAME == this.login &&
                ADMIN_PASSWORD == String(this.password)) {
            this.roles.addAll(Arrays.asList(
                    SystemRole.ADMIN, SystemRole.SHADOW))
            return true
        }
        return false
    }

    companion object {

        var system: SystemSessions? = null

        internal const val ADMIN_USERNAME = "admin"
        private const val ADMIN_PASSWORD = "admin"
    }
}
