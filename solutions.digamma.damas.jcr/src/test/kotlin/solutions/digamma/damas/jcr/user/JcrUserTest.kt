package solutions.digamma.damas.jcr.user

import org.junit.After
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.user.User

class JcrUserTest: WeldTest() {

    private val manager = WeldTest.inject(JcrUserManager::class.java)
    private lateinit var user: JcrUser

    @Before
    fun setUp() {
        this.login()
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        this.user = this.manager.create(user) as JcrUser
    }

    @After
    fun tearDown() {
        this.manager.delete(this.user.id)
        this.logout()
    }

    @Test
    fun checkPassword() {
        val password = "P@ssw0rd"
        this.user.setPassword(password)
        assert(this.user.checkPassword(password))
        assert(!this.user.checkPassword("p@ssw0rd"))
    }
}