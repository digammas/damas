package solutions.digamma.damas.jcr.content

import org.junit.After
import org.junit.Before
import org.junit.Test
import solutions.digamma.damas.common.CompatibilityException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.DocumentManager
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.jcr.Mocks
import solutions.digamma.damas.jcr.WeldTest

/**
 * Comment manager test suite.
 *
 * Created by Ahmad on 10/8/17.
 */
class JcrCommentManagerTest : WeldTest() {

    private val manager = WeldTest.inject(JcrCommentManager::class.java)
    private var token: Token? = null
    private var folder: Folder? = null
    private var document: Document? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val fm = WeldTest.inject(JcrFolderManager::class.java)
        val dm = WeldTest.inject(DocumentManager::class.java)
        this.token = this.login.login("admin", "admin")
        var parentId = fm
                .find(this.token!!).objects.iterator().next().id
        this.folder = fm.create(this.token!!, Mocks.folder(parentId, "test"))
        parentId = this.folder!!.id
        this.document = dm
                .create(this.token, Mocks.document(parentId, "file.tst"))
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        val fm = WeldTest.inject(JcrFolderManager::class.java)
        val dm = WeldTest.inject(DocumentManager::class.java)
        dm.delete(this.token!!, this.document!!.id)
        fm.delete(this.token!!, this.folder!!.id)
        this.login.logout(this.token)
    }

    @Test
    @Throws(Exception::class)
    fun retrieve() {
        val text = "Hello Comment"
        val id = manager.create(
                this.token!!,
                Mocks.comment(this.document!!.id, text, 1L)
        ).id
        val comment = manager.retrieve(this.token!!, id)
        assert(text == comment.text)
        assert(comment.rank == 1L)
        manager.delete(this.token!!, id)
    }

    @Test
    @Throws(Exception::class)
    fun create() {
        val text = "Hello Comment"
        val comment = manager.create(
                this.token!!,
                Mocks.comment(this.document!!.id, text, 1L))
        assert(text == comment.text)
        assert(comment.rank == 1L)
        manager.delete(this.token!!, comment.id)
    }

    @Test(expected = CompatibilityException::class)
    @Throws(Exception::class)
    fun createWithError() {
        val id = manager.create(
                this.token!!,
                Mocks.comment(this.folder!!.id, "Hello Comment", 1L)).id
        manager.delete(this.token!!, id)
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        var text = "Hello Comment"
        val id = manager.create(
                this.token!!,
                Mocks.comment(this.document!!.id, text, 1L)
        ).id
        text = "Salut commentaire"
        manager.update(
                this.token!!,
                id,
                Mocks.comment(this.document!!.id, text, 0L))
        val comment = manager.retrieve(this.token!!, id)
        assert(text == comment.text)
        assert(comment.rank == 0L)
        manager.delete(this.token!!, id)
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        val text = "Hello Comment"
        val id = manager.create(
                this.token!!,
                Mocks.comment(this.document!!.id, text, 1L)
        ).id
        manager.delete(this.token!!, id)
        try {
            manager.retrieve(this.token!!, id)
            assert(false)
        } catch (e: NotFoundException) { }
    }

    @Test
    @Throws(Exception::class)
    fun find() {
    }
}