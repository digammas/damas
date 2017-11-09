package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jaas.PasswordBasedLoginModule
import solutions.digamma.damas.jcr.sys.SystemSessions
import solutions.digamma.damas.jcr.user.JcrSubject
import solutions.digamma.damas.jcr.user.JcrUser
import java.security.Principal
import javax.security.auth.login.AccountLockedException
import javax.security.auth.login.AccountNotFoundException
import javax.security.auth.login.CredentialNotFoundException
import javax.security.auth.login.FailedLoginException
import javax.security.auth.login.LoginException

/**
 * @author Ahmad Shahwan
 */
open internal class UserLoginModule : PasswordBasedLoginModule() {

    private var roles: List<String> = ArrayList()

    @Throws(LoginException::class)
    override fun login(): Boolean {
        /* If system sessions not yes set, bypass this login module. */
        system ?: return false
        this.login ?: throw CredentialNotFoundException("Missing login")
        this.password ?: throw CredentialNotFoundException("Missing password")
        if (ADMIN_USERNAME == this.login &&
                ADMIN_PASSWORD == this.password.toString()) {
            this.roles = listOf("admin")
            return true
        }
        val ro = system!!.readonly
        try {
            val user = JcrUser.of(ro.getNode("${JcrSubject.ROOT_PATH}/$login"))
            user.isEnabled || throw AccountLockedException("Account disabled")
            user.checkPassword(password.toString()) ||
                    throw FailedLoginException("Invalid password")
            this.roles = user.memberships
            return true
        } catch (_: NotFoundException) {
            throw AccountNotFoundException("No such login")
        } catch (_: InternalStateException) {
            throw AccountNotFoundException("Account is not a user")
        }
    }

    @Throws(LoginException::class)
    override fun commit(): Boolean {
        val principals = this.subject?.principals
        if (principals != null) {
            this.roles.forEach({
                principals.add(Principal { it })
            })
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
