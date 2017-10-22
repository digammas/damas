package solutions.digamma.damas.jcr

import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import solutions.digamma.damas.auth.LoginManager

/**
 * @author Ahmad Shahwan
 */
open class WeldTest {

    protected var login: LoginManager? = null

    @Before
    @Throws(Exception::class)
    fun setUpWeld() {
        this.login = inject(LoginManager::class.java)
    }

    @After
    @Throws(Exception::class)
    fun tearDownWeld() {
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
