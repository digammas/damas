package solutions.digamma.damas.jcr.content

import org.junit.After
import org.junit.Before
import org.junit.Test
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.content.FolderManager
import solutions.digamma.damas.jcr.Mocks
import solutions.digamma.damas.jcr.WeldTest

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
        val rootId = this.manager.find().objects.iterator().next().id
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
        val rootId = this.manager.find().objects.iterator().next().id
        val folder = manager.create(Mocks.folder(rootId, name))
        assert(name == folder.name) { "Folder name mismatch" }
        assert(rootId == folder.parentId) { "Folder ID mismatch" }
        manager.delete(folder.id)
    }

    @Test
    @Throws(Exception::class)
    fun createWithWhiteSpace() {
        val name = "test folder"
        val rootId = this.manager.find().objects.iterator().next().id
        val folder = manager.create(Mocks.folder(rootId, name))
        assert(name == folder.name) { "Folder name mismatch" }
        assert(rootId == folder.parentId) { "Folder ID mismatch" }
        manager.delete(folder.id)
    }

    @Test
    @Throws(Exception::class)
    fun updateName() {
        var name = "test"
        val rootId = this.manager.find().objects.iterator().next().id
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
        val rootId = this.manager.find().objects.iterator().next().id
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
        val rootId = this.manager.find().objects.iterator().next().id
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
        val rootId = this.manager.find().objects.iterator().next().id
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
        val rootId = this.manager.find().objects.iterator().next().id
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
        val rootId = this.manager.find().objects.iterator().next().id
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
}