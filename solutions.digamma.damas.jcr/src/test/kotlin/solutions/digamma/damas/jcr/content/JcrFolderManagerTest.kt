package solutions.digamma.damas.jcr.content

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jcr.Mocks
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.search.Filter

/**
 * Test folder manager.
 *
 * Created by Ahmad on 9/3/17.
 */
class JcrFolderManagerTest : WeldTest() {

    private var manager = WeldTest.inject(JcrFolderManager::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        this.login()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        this.logout()
    }

    @Test
    @Throws(Exception::class)
    fun retrieve() {
        val name = "test"
        val rootId = this.manager.find("/").id
        val id = manager.create(Mocks.folder(rootId, name)).id
        val folder = manager.retrieve(id)
        assert(name == folder.name) { "Folder name mismatch" }
        assert(rootId == folder.parentId) { "Folder ID mismatch" }
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun create() {
        val name = "test"
        val rootId = this.manager.find("/").id
        val folder = manager.create(Mocks.folder(rootId, name))
        assert(name == folder.name) { "Folder name mismatch" }
        assert(rootId == folder.parentId) { "Folder ID mismatch" }
        manager.delete(folder.id)
    }

    @Test
    @Throws(Exception::class)
    fun createWithWhiteSpace() {
        val name = "test folder"
        val rootId = this.manager.find("/").id
        val folder = manager.create(Mocks.folder(rootId, name))
        assert(name == folder.name) { "Folder name mismatch" }
        assert(rootId == folder.parentId) { "Folder ID mismatch" }
        manager.delete(folder.id)
    }

    @Test
    @Throws(Exception::class)
    fun updateName() {
        var name = "test"
        val rootId = this.manager.find("/").id
        val id = manager.create(Mocks.folder(rootId, name)).id
        name = "new.txt"
        manager.update(id, Mocks.folder(null, name))
        val folder = manager.retrieve(id)
        assert(id == folder.id) { "Updated folder ID mismatch." }
        assert(name == folder.name) { "Updated folder name mismatch." }
        assert(rootId == folder.parentId) {
            "Updated folder parent ID mismatch."
        }
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun updateNameWithWhiteSpace() {
        var name = "test"
        val rootId = this.manager.find("/").id
        val id = manager.create(Mocks.folder(rootId, name)).id
        name = "new folder.txt"
        manager.update(id, Mocks.folder(null, name))
        val folder = manager.retrieve(id)
        assert(id == folder.id) { "Updated folder ID mismatch." }
        assert(name == folder.name) { "Updated folder name mismatch." }
        assert(rootId == folder.parentId) {
            "Updated folder parent ID mismatch."
        }
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun updateParent() {
        val name = "test"
        val rootId = this.manager.find("/").id
        val id = manager.create(Mocks.folder(rootId, name)).id
        val parentId = manager.create(Mocks.folder(rootId, "parent")).id
        manager.update(id, Mocks.folder(parentId, null))
        val folder = manager.retrieve(id)
        assert(id == folder.id) { "Updated folder ID mismatch." }
        assert(name == folder.name) { "Updated folder name mismatch." }
        assert(parentId == folder.parentId) {
            "Updated folder parent ID mismatch."
        }
        manager.delete(id)
        manager.delete(parentId)
    }

    @Test
    @Throws(Exception::class)
    fun updateNameAndParent() {
        var name = "test"
        val rootId = this.manager.find("/").id
        val id = manager.create(Mocks.folder(rootId, name)).id
        val parentId = manager.create(Mocks.folder(rootId, "parent")).id
        name = "new"
        manager.update(id, Mocks.folder(parentId, name))
        val folder = manager.retrieve(id)
        assert(id == folder.id) { "Updated folder ID mismatch." }
        assert(name == folder.name) { "Updated folder name mismatch." }
        assert(parentId == folder.parentId) {
            "Updated folder parent ID mismatch."
        }
        manager.delete(id)
        manager.delete(parentId)
    }

    @Test
    @Throws(Exception::class)
    fun copy() {
        val name = "test"
        val rootId = this.manager.find("/").id
        val id = manager.create(Mocks.folder(rootId, name)).id
        val parentId = manager.create(Mocks.folder(rootId, "parent")).id
        this.commit()
        val copyId = manager.copy(id, parentId).id
        val folder = manager.retrieve(copyId)
        assert(parentId == folder.parentId) { "Copied folder parent mismatch." }
        assert(name == folder.name) { "Copied folder name mismatch." }
        manager.delete(id)
        manager.delete(parentId)
    }


    @Test
    @Throws(Exception::class)
    fun delete() {
        val name = "test"
        val rootId = this.manager.find("/").id
        val id = manager.create(Mocks.folder(rootId, name)).id
        manager.delete(id)
        try {
            manager.retrieve(id)
            assert(false) { "NotFoundException should be thrown." }
        } catch (e: NotFoundException) {
            "${e.javaClass} thrown instead of NotFoundException."
        }
    }

    @Test
    @Throws(Exception::class)
    fun find() {
    }

    @Test
    fun findByName() {
        val name = "test"
        val rootId = this.manager.find("/").id
        val id = manager.create(Mocks.folder(rootId, name)).id
        this.commit()
        val filter = Mockito.mock(Filter::class.java)
        Mockito.`when`(filter.namePattern).thenReturn("te*")
        var page = this.manager.find(0, 30, filter)
        assert(page.total == 1)
        assert(page.size == 1)
        assert(page.objects.size == 1)
        val entity = page.objects.iterator().next()
        assert(name == entity.name)
        Mockito.`when`(filter.namePattern).thenReturn("toast*")
        page = this.manager.find(0, 30, filter)
        assert(page.total == 0)
        assert(page.size == 0)
        assert(page.objects.size == 0)
        this.manager.delete(id)
    }
}
