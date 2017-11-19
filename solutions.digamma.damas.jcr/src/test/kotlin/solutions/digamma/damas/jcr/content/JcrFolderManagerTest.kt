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
    private lateinit var token: Token

    @Before
    @Throws(Exception::class)
    fun setUp() {
        this.token = this.login.login("admin", "admin")
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        this.login.logout(this.token)
    }

    @Test
    @Throws(Exception::class)
    fun retrieve() {
        val name = "test"
        val rootId = this.manager
                .find(this.token).objects.iterator().next().id
        val id = manager.create(this.token, Mocks.folder(rootId, name)).id
        val folder = manager.retrieve(this.token, id)
        assert(name == folder.name) { "Folder name mismatch" }
        assert(rootId == folder.parentId) { "Folder ID mismatch" }
        manager.delete(this.token, id)
    }

    @Test
    @Throws(Exception::class)
    fun create() {
        val name = "test"
        val rootId = this.manager
                .find(this.token).objects.iterator().next().id
        val folder = manager.create(this.token, Mocks.folder(rootId, name))
        assert(name == folder.name) { "Folder name mismatch" }
        assert(rootId == folder.parentId) { "Folder ID mismatch" }
        manager.delete(this.token, folder.id)
    }

    @Test
    @Throws(Exception::class)
    fun updateName() {
        var name = "test"
        val rootId = this.manager
                .find(this.token).objects.iterator().next().id
        val id = manager.create(this.token, Mocks.folder(rootId, name)).id
        name = "new.txt"
        manager.update(this.token, id, Mocks.folder(null, name))
        val folder = manager.retrieve(this.token, id)
        assert(id == folder.id) { "Updated folder ID mismatch." }
        assert(name == folder.name) { "Updated folder name mismatch." }
        assert(rootId == folder.parentId) {
            "Updated folder parent ID mismatch."
        }
        manager.delete(this.token, id)
    }

    @Test
    @Throws(Exception::class)
    fun updateParent() {
        val name = "test"
        val rootId = this.manager
                .find(this.token).objects.iterator().next().id
        val id = manager.create(this.token, Mocks.folder(rootId, name)).id
        val parentId = manager
                .create(this.token, Mocks.folder(rootId, "parent"))
                .id
        manager.update(this.token, id, Mocks.folder(parentId, null))
        val folder = manager.retrieve(this.token, id)
        assert(id == folder.id) { "Updated folder ID mismatch." }
        assert(name == folder.name) { "Updated folder name mismatch." }
        assert(parentId == folder.parentId) {
            "Updated folder parent ID mismatch."
        }
        manager.delete(this.token, id)
        manager.delete(this.token, parentId)
    }

    @Test
    @Throws(Exception::class)
    fun updateNameAndParent() {
        var name = "test"
        val rootId = this.manager
                .find(this.token).objects.iterator().next().id
        val id = manager.create(this.token, Mocks.folder(rootId, name)).id
        val parentId = manager
                .create(this.token, Mocks.folder(rootId, "parent"))
                .id
        name = "new"
        manager.update(this.token, id, Mocks.folder(parentId, name))
        val folder = manager.retrieve(this.token, id)
        assert(id == folder.id) { "Updated folder ID mismatch." }
        assert(name == folder.name) { "Updated folder name mismatch." }
        assert(parentId == folder.parentId) {
            "Updated folder parent ID mismatch."
        }
        manager.delete(this.token, id)
        manager.delete(this.token, parentId)
    }


    @Test
    @Throws(Exception::class)
    fun delete() {
        val name = "test"
        val rootId = this.manager
                .find(this.token).objects.iterator().next().id
        val id = manager.create(this.token, Mocks.folder(rootId, name)).id
        manager.delete(this.token, id)
        try {
            manager.retrieve(this.token, id)
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