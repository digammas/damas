package solutions.digamma.damas.jcr.user

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.common.InvalidArgumentException
import solutions.digamma.damas.common.NotFoundException
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
        val entity = this.manager.create(this.token, user)
        assert(user.login == entity.login)
        assert(user.firstName == entity.firstName)
        assert(user.lastName == entity.lastName)
    }

    @Test
    fun update() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        Mockito.`when`(user.firstName).thenReturn("Mister")
        Mockito.`when`(user.lastName).thenReturn("Tester")
        val id = this.manager.create(this.token, user).id
        Mockito.`when`(user.firstName).thenReturn("Master")
        Mockito.`when`(user.lastName).thenReturn("Taster")
        this.manager.update(this.token, id, user)
        val entity = this.manager.retrieve(this.token, id)
        assert(user.login == entity.login)
        assert(user.firstName == entity.firstName)
        assert(user.lastName == entity.lastName)
    }

    @Test
    fun retrieve() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        Mockito.`when`(user.firstName).thenReturn("Mister")
        Mockito.`when`(user.lastName).thenReturn("Tester")
        val id = this.manager.create(this.token, user).id
        val entity = this.manager.retrieve(this.token, id)
        assert(user.login == entity.login)
        assert(user.firstName == entity.firstName)
        assert(user.lastName == entity.lastName)
    }

    @Test
    fun delete() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        val id = this.manager.create(this.token, user).id
        this.manager.delete(this.token, id)
        try {
            this.manager.retrieve(this.token, id)
            assert(false)
        } catch (_: NotFoundException) {
            assert(true)
        }
    }

    @Test
    fun find() {
    }

    @Test
    fun updatePassword() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        val id = this.manager.create(this.token, user).id
        this.manager.updatePassword(this.token, id, "P@55w0rd")
        try {
            /** Password too short */
            this.manager.updatePassword(this.token, id, "pass")
            assert(false)
        } catch (_: InvalidArgumentException) {
            assert(true)
        }
    }
}