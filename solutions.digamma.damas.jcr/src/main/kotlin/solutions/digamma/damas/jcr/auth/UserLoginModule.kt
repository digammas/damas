package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jcr.sys.SystemSessions
import solutions.digamma.damas.jcr.user.JcrSubject
import solutions.digamma.damas.jcr.user.JcrUser
import java.security.Principal
import javax.security.auth.Subject
import javax.security.auth.callback.CallbackHandler
import javax.security.auth.callback.NameCallback
import javax.security.auth.callback.PasswordCallback
import javax.security.auth.callback.UnsupportedCallbackException
import javax.security.auth.login.AccountLockedException
import javax.security.auth.login.AccountNotFoundException
import javax.security.auth.login.CredentialNotFoundException
import javax.security.auth.login.FailedLoginException
import javax.security.auth.login.LoginException
import javax.security.auth.spi.LoginModule

/**
 * @author Ahmad Shahwan
 */
open internal class UserLoginModule : LoginModule {

    protected var subject: Subject? = null
    private var callbackHandler: CallbackHandler? = null
    protected var login: String? = null
    protected var password: CharArray? = null
    private var sharedState: Map<String, *>? = null
    private var roles: List<String> = ArrayList()

    override fun initialize(
            subject: Subject,
            callbackHandler: CallbackHandler,
            sharedState: Map<String, *>,
            options: Map<String, *>) {
        this.subject = subject
        this.callbackHandler = callbackHandler
        this.sharedState = sharedState
        this.extractCredentials()
        this.roles = ArrayList()
    }

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

    @Throws(LoginException::class)
    override fun abort(): Boolean {
        return true
    }

    @Throws(LoginException::class)
    override fun logout(): Boolean {
        this.subject?.principals?.clear()
        return true
    }

    private fun extractCredentials() {
        val userObj = sharedState!![USERNAME]
        val passObj = sharedState!![PASSWORD]
        when (userObj) {
            is Principal -> this.login = userObj.name
            null -> {
                val callback = NameCallback("username")
                try {
                    this.callbackHandler!!.handle(arrayOf(callback))
                    this.login = callback.name
                } catch (_: UnsupportedCallbackException) {
                    this.login = null
                }
            }
            else -> this.login = userObj.toString()
        }
        when (passObj) {
            is CharArray -> this.password = passObj
            null -> {
                val callback = PasswordCallback("password", false)
                try {
                    this.callbackHandler!!.handle(arrayOf(callback))
                    this.password = callback.password
                } catch (_: UnsupportedCallbackException) {
                    this.password = null
                }
            }
            else -> this.password = passObj.toString().toCharArray()
        }
    }

    companion object {

        var system: SystemSessions? = null

        private val USERNAME = "javax.security.auth.login.name"
        private val PASSWORD = "javax.security.auth.login.password"

        private val ADMIN_USERNAME = "admin"
        private val ADMIN_PASSWORD = "admin"
    }
}
