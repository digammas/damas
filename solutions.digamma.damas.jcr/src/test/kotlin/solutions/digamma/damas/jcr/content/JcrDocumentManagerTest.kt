package solutions.digamma.damas.jcr.content

import org.junit.After
import org.junit.Before
import org.junit.Test
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.content.DocumentManager
import solutions.digamma.damas.content.FolderManager
import solutions.digamma.damas.jcr.Mocks
import solutions.digamma.damas.jcr.WeldTest

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

/**
 * Test scenarios for [JcrDocumentManager].
 *
 * Created by Ahmad on 9/2/17.
 */
class JcrDocumentManagerTest : WeldTest() {

    private var manager = WeldTest.inject(JcrDocumentManager::class.java)
    private var folderManager = WeldTest.inject(FolderManager::class.java)

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
    fun create() {
        val name = "test.txt"
        val rootId = this.folderManager.find().objects.iterator().next().id
        val document = manager.create(Mocks.document(rootId, name))
        assert(name == document.name) { "Document name mismatch" }
        assert(rootId == document.parentId) { "Document ID mismatch" }
        manager.delete(document.id)
    }

    @Test
    @Throws(Exception::class)
    fun createWithWhiteSpace() {
        val name = "test file.txt"
        val rootId = this.folderManager.find().objects.iterator().next().id
        val document = manager.create(Mocks.document(rootId, name))
        assert(name == document.name) { "Document name mismatch" }
        assert(rootId == document.parentId) { "Document ID mismatch" }
        manager.delete(document.id)
    }

    @Test
    @Throws(Exception::class)
    fun download() {
        val name = "test.txt"
        val rootId = this.folderManager
                .find().objects.iterator().next().id
        val document = manager.create(Mocks.document(rootId, name))
        val id = document.id
        val text = "Hello"
        val upload = ByteArrayInputStream(
                text.toByteArray(StandardCharsets.UTF_8))
        manager.upload(id, upload)
        val download = manager.download(id).stream
        val readText = BufferedReader(InputStreamReader(download))
                .lines()
                .collect(Collectors.joining("\n"))
        assert(text == readText) { "Downloaded vs uploaded text mismatch." }
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun upload() {
        val name = "test.txt"
        val rootId = this.folderManager.find().objects.iterator().next().id
        val document = manager.create(Mocks.document(rootId, name))
        val id = document.id
        val input = ByteArrayInputStream(
                "Hello".toByteArray(StandardCharsets.UTF_8))
        manager.upload(id, input)
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun retrieve() {
        val name = "test.txt"
        val rootId = this.folderManager.find().objects.iterator().next().id
        val id = manager.create(Mocks.document(rootId, name)).id
        val document = manager.retrieve(id)
        assert(id == document.id) { "Retrieved document ID mismatch." }
        assert(name == document.name) { "Retrieved document name mismatch." }
        assert(rootId == document.parentId) {
            "Retrieved document parent ID mismatch."
        }
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun updateName() {
        var name = "test.txt"
        val rootId = this.folderManager.find().objects.iterator().next().id
        val id = manager.create(Mocks.document(rootId, name)).id
        name = "new.txt"
        manager.update(id, Mocks.document(null, name))
        val document = manager.retrieve(id)
        assert(id == document.id) { "Updated document ID mismatch." }
        assert(name == document.name) { "Updated document name mismatch." }
        assert(rootId == document.parentId) {
            "Updated document parent ID mismatch."
        }
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun updateNameWithWhiteSpace() {
        var name = "test.txt"
        val rootId = this.folderManager.find().objects.iterator().next().id
        val id = manager.create(Mocks.document(rootId, name)).id
        name = "new name.txt"
        manager.update(id, Mocks.document(null, name))
        val document = manager.retrieve(id)
        assert(id == document.id) { "Updated document ID mismatch." }
        assert(name == document.name) { "Updated document name mismatch." }
        assert(rootId == document.parentId) {
            "Updated document parent ID mismatch."
        }
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun updateParent() {
        val name = "test.txt"
        val rootId = this.folderManager.find().objects.iterator().next().id
        val id = manager.create(Mocks.document(rootId, name)).id
        val folderId = folderManager.create(Mocks.folder(rootId, "test")).id
        manager.update(id, Mocks.document(folderId, null))
        val document = manager.retrieve(id)
        assert(id == document.id) { "Updated document ID mismatch." }
        assert(name == document.name) { "Updated document name mismatch." }
        assert(folderId == document.parentId) {
            "Updated document parent ID mismatch."
        }
        manager.delete(id)
        folderManager.delete(folderId)
    }

    @Test
    @Throws(Exception::class)
    fun updateNameAndParent() {
        var name = "test.txt"
        val rootId = this.folderManager.find().objects.iterator().next().id
        val id = manager.create(Mocks.document(rootId, name)).id
        val folderId = folderManager.create(Mocks.folder(rootId, "test")).id
        name = "new.txt"
        manager.update(id, Mocks.document(folderId, name))
        val document = manager.retrieve(id)
        assert(id == document.id) { "Updated document ID mismatch." }
        assert(name == document.name) { "Updated document name mismatch." }
        assert(folderId == document.parentId) {
            "Updated document parent ID mismatch."
        }
        manager.delete(id)
        folderManager.delete(folderId)
    }

    @Test
    @Throws(Exception::class)
    fun updateMimeType() {
        val rootId = this.folderManager.find().objects.iterator().next().id
        val mock = Mocks.document(rootId, "test.txt")
        val mtTextPlain = "text/plain"
        Mocks.on(mock.mimeType, mtTextPlain)
        val id = manager.create(mock).id
        assert(manager.retrieve(id).mimeType == mtTextPlain) {
            "Wrong mime-type on creation."
        }
        val mtTextHtml = "text/html"
        val updater = Mocks.document()
        Mocks.on(updater.mimeType, mtTextHtml)
        manager.update(id, updater)
        assert(manager.retrieve(id).mimeType == mtTextHtml) {
            "Wrong mime-type on modification."
        }
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        val name = "test.txt"
        val rootId = this.folderManager.find().objects.iterator().next().id
        val id = manager.create(Mocks.document(rootId, name)).id
        manager.delete(id)
        try {
            manager.retrieve(id)
            assert(false) { "NotFoundException should be thrown." }
        } catch (e: NotFoundException) {
            assert(true) {
                "${e.javaClass} thrown instead of NotFoundException."
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun find() {
    }
}