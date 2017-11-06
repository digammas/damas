package solutions.digamma.damas.jcr.auth

import java.security.Principal
import javax.security.auth.Subject
import javax.security.auth.callback.CallbackHandler
import javax.security.auth.callback.NameCallback
import javax.security.auth.callback.PasswordCallback
import javax.security.auth.callback.UnsupportedCallbackException
import javax.security.auth.login.LoginException
import javax.security.auth.spi.LoginModule

/**
 * @author Ahmad Shahwan
 */
open internal class UserLoginModule : LoginModule {

    private var subject: Subject? = null
    private var callbackHandler: CallbackHandler? = null
    protected var login: String? = null
    protected var password: CharArray? = null
    private var sharedState: Map<String, *>? = null
    protected var roles: MutableList<String> = ArrayList()

    override fun initialize(
            subject: Subject,
            callbackHandler: CallbackHandler,
            sharedState: Map<String, *>,
            options: Map<String, *>) {
        this.subject = subject
        this.callbackHandler = callbackHandler
        this.sharedState = sharedState
        this.extractCredentials()
    }

    @Throws(LoginException::class)
    override fun login(): Boolean {
        this.login ?: throw LoginException("Missing login")
        this.password ?: throw LoginException("Missing password")
        return false
    }

    @Throws(LoginException::class)
    override fun commit(): Boolean {
        return false
    }

    @Throws(LoginException::class)
    override fun abort(): Boolean {
        return false
    }

    @Throws(LoginException::class)
    override fun logout(): Boolean {
        return false
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

        private val USERNAME = "javax.security.auth.login.name"
        private val PASSWORD = "javax.security.auth.login.password"
    }
}
