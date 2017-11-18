package solutions.digamma.damas.jcr.auth

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.user.User
import solutions.digamma.damas.user.UserManager

class JcrPermissionManagerTest  : WeldTest() {

    private val userManager = WeldTest.inject(UserManager::class.java)
    private lateinit var token: Token
    private lateinit var username: String
    private lateinit var userToken: Token

    @Before
    fun setUp() {
        this.token = this.login.login("admin", "admin")
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        this.username = this.userManager.create(this.token, user).login
        val password = "P@55w0rd"
        this.userManager.updatePassword(this.token, this.username, password)
        this.userToken = this.login.login(this.username, password)
    }

    @After
    fun tearDown() {
        this.login.logout(this.userToken)
        this.userManager.delete(this.token, this.username)
        this.login.logout(this.token)
    }


    @Test
    fun retrieve() {
    }

    @Test
    fun retrieveAt() {
    }

    @Test
    fun updateAt() {
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun create() {
    }

}