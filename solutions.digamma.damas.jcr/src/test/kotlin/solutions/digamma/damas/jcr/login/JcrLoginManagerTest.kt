package solutions.digamma.damas.jcr.login

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.user.User
import solutions.digamma.damas.user.UserManager

class JcrLoginManagerTest : WeldTest() {

    private val userManager = WeldTest.inject(UserManager::class.java)
    private lateinit var token: Token
    private lateinit var username: String
    private val password = "P@55w0rd"
    private lateinit var manager: JcrLoginManager

    @Before
    fun setUp() {
        this.manager = inject(JcrLoginManager::class.java)
        this.token = this.login.login("admin", "admin")
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        this.username = this.userManager.create(this.token, user).login
        this.userManager
                .updatePassword(this.token, this.username, this.password)
    }

    @After
    fun tearDown() {
        this.userManager.delete(this.token, this.username)
        this.login.logout(this.token)
    }

    @Test
    fun login() {
        this.manager.login(this.username, this.password)
    }

    @Test
    fun logout() {
        val token = this.manager.login(this.username, this.password)
        this.manager.logout(token)
    }
}