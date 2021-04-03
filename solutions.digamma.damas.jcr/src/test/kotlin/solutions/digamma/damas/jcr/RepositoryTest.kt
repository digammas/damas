package solutions.digamma.damas.jcr

import org.junit.After
import org.junit.Before
import solutions.digamma.damas.cdi.ContainerTest
import solutions.digamma.damas.login.LoginManager
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.session.Transaction
import solutions.digamma.damas.session.TransactionManager

/**
 * @author Ahmad Shahwan
 */
open class RepositoryTest : ContainerTest() {

    protected lateinit var login: LoginManager
    protected lateinit var authenticator: TransactionManager

    private var token: Token? = null
    private var transaction: Transaction? = null


    @Before
    @Throws(Exception::class)
    fun setUpWeld() {
        this.login = inject(LoginManager::class.java)
        this.authenticator = inject(TransactionManager::class.java)
    }

    @After
    @Throws(Exception::class)
    fun tearDownWeld() {
    }

    protected fun login() {
        this.token = this.login.login("admin", "admin")
        this.transaction = authenticator.begin(this.token)
    }

    protected fun logout() {
        this.token?.let { this.login.logout(it) }
        this.transaction?.close()
    }

    protected fun commit() {
        this.transaction?.commit()
    }
}
