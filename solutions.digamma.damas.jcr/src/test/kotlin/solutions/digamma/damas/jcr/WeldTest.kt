package solutions.digamma.damas.jcr

import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import solutions.digamma.damas.login.Authentication
import solutions.digamma.damas.login.AuthenticationManager
import solutions.digamma.damas.login.LoginManager
import solutions.digamma.damas.login.Token

/**
 * @author Ahmad Shahwan
 */
open class WeldTest {

    protected lateinit var login: LoginManager
    protected lateinit var authenticator: AuthenticationManager

    private var token: Token? = null
    protected var auth: Authentication? = null


    @Before
    @Throws(Exception::class)
    fun setUpWeld() {
        this.login = inject(LoginManager::class.java)
        this.authenticator = inject(AuthenticationManager::class.java)
    }

    @After
    @Throws(Exception::class)
    fun tearDownWeld() {
    }

    protected fun login() {
        this.token = this.login.login("admin", "admin")
        this.auth = authenticator.authenticate(this.token)
    }

    protected fun logout() {
        this.login.logout(this.token)
        this.auth?.close()
    }

    protected fun commit() {
        this.auth?.close()
        this.auth = authenticator.authenticate(this.token)
    }

    companion object {

        private val WELD = WeldRule()

        @JvmStatic
        @ClassRule
        fun weld() = WELD

        fun <T> inject(klass: Class<T>): T {
            return weld().inject(klass)
        }
    }

}
