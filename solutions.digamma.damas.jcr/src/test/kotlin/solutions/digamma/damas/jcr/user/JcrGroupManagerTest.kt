package solutions.digamma.damas.jcr.user

import org.junit.After
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.user.Group

class JcrGroupManagerTest: WeldTest() {

    private val manager = WeldTest.inject(JcrGroupManager::class.java)

    @Before
    fun setUp() {
        this.login()
    }

    @After
    fun tearDown() {
        this.logout()
    }


    @Test
    fun create() {
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val entity = this.manager.create(group)
        assert(group.name == entity.name)
        this.manager.delete(entity.id)
    }

    @Test
    fun update() {
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val id = this.manager.create(group).id
        Mockito.`when`(group.name).thenReturn("tasters")
        this.manager.update(id, group)
        val entity = this.manager.retrieve(id)
        assert(group.name == entity.name)
        this.manager.delete(id)
    }

    @Test
    fun retrieve() {
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val id = this.manager.create(group).id
        val entity = this.manager.retrieve(id)
        assert(group.name == entity.name)
        this.manager.delete(id)
    }

    @Test
    fun delete() {
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val id = this.manager.create(group).id
        this.manager.delete(id)
        try {
            this.manager.retrieve(id)
            assert(false)
        } catch (_: NotFoundException) {
            assert(true)
        }
    }

    @Test
    fun find() {
    }
}