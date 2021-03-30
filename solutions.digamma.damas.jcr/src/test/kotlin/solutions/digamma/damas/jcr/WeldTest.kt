package solutions.digamma.damas.jcr

import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import solutions.digamma.damas.session.Transaction
import solutions.digamma.damas.session.TransactionManager
import solutions.digamma.damas.login.LoginManager
import solutions.digamma.damas.login.Token

/**
 * @author Ahmad Shahwan
 */
open class WeldTest {

    protected lateinit var login: LoginManager
    protected lateinit var authenticator: TransactionManager

    private var token: Token? = null
    protected var transaction: Transaction? = null


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
