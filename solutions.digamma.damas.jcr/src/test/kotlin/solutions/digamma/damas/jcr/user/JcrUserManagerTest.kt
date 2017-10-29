package solutions.digamma.damas.jcr.user

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.user.User

class JcrUserManagerTest: WeldTest() {

    private val manager = WeldTest.inject(JcrUserManager::class.java)
    private lateinit var token: Token

    @Before
    fun setUp() {
        this.token = this.login.login("admin", "admin")
    }

    @After
    fun tearDown() {
        this.login.logout(this.token)
    }

    @Test
    fun create() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        Mockito.`when`(user.firstName).thenReturn("Mister")
        Mockito.`when`(user.lastName).thenReturn("Tester")
        this.manager.create(this.token, user)
    }

    @Test
    fun update() {
    }

    @Test
    fun retrieve() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun find() {
    }
}