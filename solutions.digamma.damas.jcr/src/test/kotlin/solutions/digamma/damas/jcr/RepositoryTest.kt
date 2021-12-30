package solutions.digamma.damas.jcr

import org.junit.After
import org.junit.Before
import solutions.digamma.damas.cdi.ContainerTest
import solutions.digamma.damas.session.LoginManager
import solutions.digamma.damas.session.Token
import solutions.digamma.damas.session.Connection
import solutions.digamma.damas.session.ConnectionManager

/**
 * @author Ahmad Shahwan
 */
open class RepositoryTest : ContainerTest() {

    protected lateinit var login: LoginManager
    protected lateinit var authenticator: ConnectionManager

    private var token: Token? = null
    private var connection: Connection? = null


    @Before
    @Throws(Exception::class)
    fun setUpWeld() {
        this.login = inject(LoginManager::class.java)
        this.authenticator = inject(ConnectionManager::class.java)
    }

    @After
    @Throws(Exception::class)
    fun tearDownWeld() {
    }

    protected fun login() {
        this.token = this.login.login("admin", "admin")
        this.connection = authenticator.connect(this.token)
    }

    protected fun logout() {
        this.token?.let { this.login.logout(it) }
        this.connection?.close()
    }

    protected fun commit() {
        this.connection?.commit()
    }
}
