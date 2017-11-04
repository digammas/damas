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
class JcrLoginModule : LoginModule {

    private var subject: Subject? = null
    private var callbackHandler: CallbackHandler? = null
    private var login: String? = null
    private var password: CharArray? = null
    private var sharedState: Map<String, *>? = null
    private val roles: List<String>? = null

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
        if (userObj is Principal)
            this.login = userObj.name
        else if (userObj != null) {
            this.login = userObj.toString()
        }
        if (passObj is CharArray)
            this.password = passObj
        else if (passObj != null) {
            this.password = passObj.toString().toCharArray()
        }
        if (this.login != null && this.password != null) {
            return
        }
        val nameCallback = NameCallback("username")
        val passwordCallback = PasswordCallback("password", false)
        try {
            this.callbackHandler!!.handle(
                    arrayOf(nameCallback, passwordCallback))
            this.login = nameCallback.name
            this.password = passwordCallback.password
        } catch (e: UnsupportedCallbackException) {
            this.login = null
            this.password = null
        }
    }

    companion object {

        private val USERNAME = "javax.security.auth.login.name"
        private val PASSWORD = "javax.security.auth.login.password"
    }
}
