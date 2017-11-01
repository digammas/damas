package solutions.digamma.damas.jcr.user

import org.junit.After
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito
import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.user.Group

class JcrGroupManagerTest: WeldTest() {

    private val manager = WeldTest.inject(JcrGroupManager::class.java)
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
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val entity = this.manager.create(this.token, group)
        assert(group.name == entity.name)
    }

    @Test
    fun update() {
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val id = this.manager.create(this.token, group).id
        Mockito.`when`(group.name).thenReturn("tasters")
        this.manager.update(this.token, id, group)
        val entity = this.manager.retrieve(this.token, id)
        assert(group.name == entity.name)
    }

    @Test
    fun retrieve() {
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val id = this.manager.create(this.token, group).id
        val entity = this.manager.retrieve(this.token, id)
        assert(group.name == entity.name)
    }

    @Test
    fun delete() {
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val id = this.manager.create(this.token, group).id
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
}