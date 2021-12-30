package solutions.digamma.damas.jcr.login

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import solutions.digamma.damas.jcr.RepositoryTest
import solutions.digamma.damas.user.User
import solutions.digamma.damas.user.UserManager

class JcrLoginManagerTest : RepositoryTest() {

    private val userManager = inject(UserManager::class.java)
    private lateinit var username: String
    private val password = "P@55w0rd"
    private lateinit var manager: JcrLoginManager

    @Before
    fun setUp() {
        this.manager = inject(JcrLoginManager::class.java)
        this.login()
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        this.username = this.userManager.create(user).login
        this.userManager
                .updatePassword(this.username, this.password)
        this.commit()
    }

    @After
    fun tearDown() {
        this.userManager.delete(this.username)
        this.logout()
    }

    @Test
    fun testLogin() {
        this.manager.login(this.username, this.password)
    }

    @Test
    fun testIdentify() {
        val token = this.manager.login(this.username, this.password)
        val session = this.manager.identify(token)
        assert(session.userLogin == this.username)
    }

    @Test
    fun testLogout() {
        val token = this.manager.login(this.username, this.password)
        this.manager.logout(token)
    }
}
