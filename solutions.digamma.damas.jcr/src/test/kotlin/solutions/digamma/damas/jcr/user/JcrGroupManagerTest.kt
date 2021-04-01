package solutions.digamma.damas.jcr.user

import org.junit.After
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.search.Filter
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
        Mockito.`when`(group.label).thenReturn("The Testers")
        val entity = this.manager.create(group)
        assert(group.name == entity.name)
        assert(group.label == entity.label)
        this.manager.delete(entity.id)
    }

    @Test
    fun update() {
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val id = this.manager.create(group).id
        Mockito.`when`(group.label).thenReturn("The Testers")
        this.manager.update(id, group)
        val entity = this.manager.retrieve(id)
        assert(group.label == entity.label)
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
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val id = this.manager.create(group).id
        this.commit()
        val page = this.manager.find()
        assert(page.total == 1)
        assert(page.size == 1)
        assert(page.objects.size == 1)
        val entity = page.objects.iterator().next()
        assert(group.name == entity.name)
        this.manager.delete(id)
    }

    @Test
    fun findByName() {
        val group = Mockito.mock(Group::class.java)
        Mockito.`when`(group.name).thenReturn("testers")
        val id = this.manager.create(group).id
        this.commit()
        val filter = Mockito.mock(Filter::class.java)
        Mockito.`when`(filter.namePattern).thenReturn("test*")
        var page = this.manager.find(0, 30, filter)
        assert(page.total == 1)
        assert(page.size == 1)
        assert(page.objects.size == 1)
        val entity = page.objects.iterator().next()
        assert(group.name == entity.name)
        Mockito.`when`(filter.namePattern).thenReturn("toast*")
        page = this.manager.find(0, 30, filter)
        assert(page.total == 0)
        assert(page.size == 0)
        assert(page.objects.size == 0)
        this.manager.delete(id)
    }
}
