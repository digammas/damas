package solutions.digamma.damas.jcr.content

import org.junit.After
import org.junit.Before
import org.junit.Test
import solutions.digamma.damas.common.CompatibilityException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.DocumentManager
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.jcr.Mocks
import solutions.digamma.damas.jcr.RepositoryTest

/**
 * Comment manager test suite.
 *
 * Created by Ahmad on 10/8/17.
 */
class JcrCommentManagerTest : RepositoryTest() {

    private val manager = inject(JcrCommentManager::class.java)
    private var folder: Folder? = null
    private var document: Document? = null
    private var secondary: Document? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val fm = inject(JcrFolderManager::class.java)
        val dm = inject(DocumentManager::class.java)
        this.login();
        var parentId = fm.find("/").id
        this.folder = fm.create(Mocks.folder(parentId, "test"))
        parentId = this.folder!!.id
        this.document = dm.create(Mocks.document(parentId, "file.tst"))
        this.secondary = dm.create(Mocks.document(parentId, "file2.tst"))
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        val fm = inject(JcrFolderManager::class.java)
        val dm = inject(DocumentManager::class.java)
        dm.delete(this.document!!.id)
        dm.delete(this.secondary!!.id)
        fm.delete(this.folder!!.id)
        this.logout()
    }

    @Test
    @Throws(Exception::class)
    fun retrieve() {
        val text = "Hello Comment"
        val id = manager.create(Mocks.comment(this.document!!.id, text, 1L)
        ).id
        val comment = manager.retrieve(id)
        assert(text == comment.text)
        assert(comment.rank == 1L)
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun create() {
        val text = "Hello Comment"
        val comment = manager.create(
                Mocks.comment(this.document!!.id, text, 1L))
        assert(text == comment.text)
        assert(comment.rank == 1L)
        manager.delete(comment.id)
    }

    @Test
    @Throws(Exception::class)
    fun createDeep() {
        val text = "Hello Comment"
        val c1 = manager.create(Mocks.comment(this.document!!.id, text, 1L))
        val c2 = manager.create(Mocks.comment(c1.id, text, 1L))
        this.commit()
        val comment1 = manager.retrieve(c1.id)
        val comment2 = manager.retrieve(c2.id)
        assert(comment1.comments.any { it.id == c2.id })
        assert(comment2.receiverId == c1.id)
        manager.delete(comment1.id)
    }

    @Test(expected = CompatibilityException::class)
    @Throws(Exception::class)
    fun createWithError() {
        val id = manager.create(
                Mocks.comment(this.folder!!.id, "Hello Comment", 1L)).id
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        var text = "Hello Comment"
        val id = manager.create(
                Mocks.comment(this.document!!.id, text, 1L)
        ).id
        text = "Salut commentaire"
        manager.update(id, Mocks.comment(this.document!!.id, text, 0L))
        val comment = manager.retrieve(id)
        assert(text == comment.text)
        assert(comment.rank == 0L)
        manager.delete(id)
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        val text = "Hello Comment"
        val id = manager.create(
                Mocks.comment(this.document!!.id, text, 1L)
        ).id
        this.commit()
        manager.delete(id)
        try {
            manager.retrieve(id)
            assert(false)
        } catch (e: NotFoundException) { }
    }

    @Test
    @Throws(Exception::class)
    fun find() {
        val text1 = "Hello"
        val text2 = "Hi"
        val c1 = manager.create(Mocks.comment(this.document!!.id, text1, 1L))
        manager.create(Mocks.comment(this.secondary!!.id, text2, 1L))
        this.commit()
        val page = manager.find(0, 10, Mocks.filter(this.document?.id!!, true))
        assert(page.size == 1)
        assert(page.objects.any { c -> c.id == c1.id })
    }

    @Test
    @Throws(Exception::class)
    fun findRecursive() {
        val text1 = "Hello"
        val text2 = "Hi"
        val c1 = manager.create(Mocks.comment(this.document!!.id, text1, 1L))
        val c2 = manager.create(Mocks.comment(c1.id, text2, 1L))
        this.commit()
        val page = manager.find(0, 10, Mocks.filter(this.document?.id!!, true))
        assert(page.size == 2)
        assert(page.objects.any { c -> c.id == c1.id })
        assert(page.objects.any { c -> c.id == c2.id })
    }

    @Test
    @Throws(Exception::class)
    fun findNonRecursive() {
        val text1 = "Hello"
        val text2 = "Hi"
        val c1 = manager.create(Mocks.comment(this.document!!.id, text1, 1L))
        manager.create(Mocks.comment(c1.id, text2, 1L))
        this.commit()
        val page = manager.find(0, 10, Mocks.filter(this.document?.id!!, false))
        assert(page.size == 1)
        assert(page.objects.any { c -> c.id == c1.id })
    }
}
