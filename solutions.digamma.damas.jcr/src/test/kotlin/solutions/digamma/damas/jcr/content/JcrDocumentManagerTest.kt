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

    private var manager: DocumentManager? = null
    private var folderManager: FolderManager? = null
    private var token: Token? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        this.manager = WeldTest.inject(JcrDocumentManager::class.java)
        this.folderManager = WeldTest.inject(FolderManager::class.java)
        this.token = this.login!!.login("admin", "admin")

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        this.login!!.logout(this.token)
    }


    @Test
    @Throws(Exception::class)
    fun create() {
        val name = "test.txt"
        val rootId = this.folderManager!!
                .find(this.token).objects.iterator().next().id
        val document = manager!!.create(this.token, Mocks.document(rootId, name))
        assert(name == document.name) { "Document name mismatch" }
        assert(rootId == document.parentId) { "Document ID mismatch" }
    }

    @Test
    @Throws(Exception::class)
    fun download() {
        val name = "test.txt"
        val rootId = this.folderManager!!
                .find(this.token).objects.iterator().next().id
        val document = manager!!.create(this.token, Mocks.document(rootId, name))
        val id = document.id
        val text = "Hello"
        val upload = ByteArrayInputStream(text.toByteArray(StandardCharsets.UTF_8))
        manager!!.upload(token, id, upload)
        val download = manager!!.download(token, id).stream
        val readText = BufferedReader(InputStreamReader(download))
                .lines()
                .collect(Collectors.joining("\n"))
        assert(text == readText) { "Downloaded vs uploaded text mismatch." }
    }

    @Test
    @Throws(Exception::class)
    fun upload() {
        val name = "test.txt"
        val rootId = this.folderManager!!
                .find(this.token).objects.iterator().next().id
        val document = manager!!.create(this.token, Mocks.document(rootId, name))
        val id = document.id
        val input = ByteArrayInputStream("Hello".toByteArray(StandardCharsets.UTF_8))
        manager!!.upload(token, id, input)
    }

    @Test
    @Throws(Exception::class)
    fun retrieve() {
        val name = "test.txt"
        val rootId = this.folderManager!!
                .find(this.token).objects.iterator().next().id
        val id = manager!!
                .create(this.token, Mocks.document(rootId, name)).id
        val document = manager!!.retrieve(token, id)
        assert(id == document.id) { "Retrieved document ID mismatch." }
        assert(name == document.name) { "Retrieved document name mismatch." }
        assert(rootId == document.parentId) { "Retrieved document parent ID mismatch." }
    }

    @Test
    @Throws(Exception::class)
    fun updateName() {
        var name = "test.txt"
        val rootId = this.folderManager!!
                .find(this.token).objects.iterator().next().id
        val id = manager!!
                .create(this.token, Mocks.document(rootId, name)).id
        name = "new.txt"
        manager!!.update(this.token, id, Mocks.document(null, name))
        val document = manager!!.retrieve(this.token, id)
        assert(id == document.id) { "Updated document ID mismatch." }
        assert(name == document.name) { "Updated document name mismatch." }
        assert(rootId == document.parentId) { "Updated document parent ID mismatch." }
    }

    @Test
    @Throws(Exception::class)
    fun updateParent() {
        val name = "test.txt"
        val rootId = this.folderManager!!
                .find(this.token).objects.iterator().next().id
        val id = manager!!
                .create(this.token, Mocks.document(rootId, name)).id
        val folderId = folderManager!!
                .create(this.token, Mocks.folder(rootId, "test"))
                .id
        manager!!.update(this.token, id, Mocks.document(folderId, null))
        val document = manager!!.retrieve(this.token, id)
        assert(id == document.id) { "Updated document ID mismatch." }
        assert(name == document.name) { "Updated document name mismatch." }
        assert(folderId == document.parentId) { "Updated document parent ID mismatch." }
    }

    @Test
    @Throws(Exception::class)
    fun updateNameAndParent() {
        var name = "test.txt"
        val rootId = this.folderManager!!
                .find(this.token).objects.iterator().next().id
        val id = manager!!
                .create(this.token, Mocks.document(rootId, name)).id
        val folderId = folderManager!!
                .create(this.token, Mocks.folder(rootId, "test"))
                .id
        name = "new.txt"
        manager!!.update(this.token, id, Mocks.document(folderId, name))
        val document = manager!!.retrieve(this.token, id)
        assert(id == document.id) { "Updated document ID mismatch." }
        assert(name == document.name) { "Updated document name mismatch." }
        assert(folderId == document.parentId) { "Updated document parent ID mismatch." }
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        val name = "test.txt"
        val rootId = this.folderManager!!
                .find(this.token).objects.iterator().next().id
        val id = manager!!
                .create(this.token, Mocks.document(rootId, name)).id
        manager!!.delete(this.token, id)
        try {
            manager!!.retrieve(this.token, id)
            assert(false) { "NotFoundException should be thrown." }
        } catch (e: NotFoundException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun find() {
    }
}