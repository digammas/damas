package solutions.digamma.damas.jcr.login

import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jaas.NamedPrincipal
import solutions.digamma.damas.jaas.AbstractLoginModule
import solutions.digamma.damas.jcr.sys.SystemRole
import solutions.digamma.damas.jcr.sys.SystemSessions
import solutions.digamma.damas.jcr.user.JcrSubject
import solutions.digamma.damas.jcr.user.JcrUser
import javax.security.auth.login.AccountLockedException
import javax.security.auth.login.AccountNotFoundException
import javax.security.auth.login.CredentialNotFoundException
import javax.security.auth.login.FailedLoginException
import javax.security.auth.login.LoginException

/**
 * @author Ahmad Shahwan
 */
open internal class UserLoginModule : AbstractLoginModule() {

    @Throws(LoginException::class)
    override fun doLogin(): Boolean {
        /* If system sessions not yes set, bypass this login module. */
        system ?: return false
        this.login ?: throw CredentialNotFoundException("Missing login")
        this.password ?: throw CredentialNotFoundException("Missing password")
        val ro = system!!.readonly
        try {
            val user = JcrUser.of(ro.getNode("${JcrSubject.ROOT_PATH}/$login"))
            user.isEnabled || throw AccountLockedException("Account disabled")
            user.checkPassword(password.toString()) ||
                    throw FailedLoginException("Invalid password")
            this.roles = user.memberships.map { NamedPrincipal(it) }
            return true
        } catch (_: NotFoundException) {
            return loginAdmin() || throw AccountNotFoundException("No such login")
        } catch (_: InternalStateException) {
            return loginAdmin() || throw AccountNotFoundException("Account is not a user")
        }
    }

    private fun loginAdmin(): Boolean {
        if (ADMIN_USERNAME == this.login &&
                ADMIN_PASSWORD == this.password.toString()) {
            this.roles = listOf(SystemRole.ADMIN)
            return true
        }
        return false
    }

    companion object {

        var system: SystemSessions? = null

        private val ADMIN_USERNAME = "admin"
        private val ADMIN_PASSWORD = "admin"
    }
}
