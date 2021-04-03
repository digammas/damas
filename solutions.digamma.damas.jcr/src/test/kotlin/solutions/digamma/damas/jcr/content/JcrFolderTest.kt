package solutions.digamma.damas.jcr.content

import org.junit.After
import org.junit.Before
import org.junit.Test
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.jcr.Mocks
import solutions.digamma.damas.jcr.RepositoryTest

/**
 * Test folder object.
 *
 * Created by Ahmad on 9/3/17.
 */
class JcrFolderTest : RepositoryTest() {

    private var folder: Folder? = null
    private var root: Folder? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val manager = inject(JcrFolderManager::class.java)
        this.login()
        this.root = manager.find("/")
        val parentId = this.root!!.id
        this.folder = manager.create(Mocks.folder(parentId, "test"))
        var parent = this.folder
        repeat(10) {
            parent = manager.create(Mocks.folder(parent!!.id, "test_$it"))
        }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        val manager = inject(JcrFolderManager::class.java)
        manager.delete(this.folder!!.id)
        this.logout()
    }

    @Test
    @Throws(Exception::class)
    fun expandContent() {
        var expanded = this.folder
        expanded!!.expandContent()
        repeat(10) {
            expanded = expanded!!.content.folders.iterator().next()
        }
    }

    @Test
    @Throws(Exception::class)
    fun expandContentToDepth() {
        var expanded = this.folder
        val depth = 5
        expanded!!.expandContent(depth)
        repeat(depth) {
            expanded = expanded!!.content.folders.iterator().next()
        }
        assert(expanded!!.content == null)
    }

    @Test
    @Throws(Exception::class)
    fun getContent() {
        this.folder!!.expandContent()
        val folders = this.folder!!.content.folders
        assert(folders.size != 0)
        val subfolder = folders.iterator().next()
        assert(subfolder.name == "test_${0}")
    }

    @Test
    fun pathIds() {
        var expanded = this.folder
        val pathIds = this.folder!!.pathIds.toMutableList()
        expanded!!.expandContent()
        repeat(10) {
            expanded = expanded!!.content.folders.iterator().next()
            pathIds.add(expanded!!.id)
        }
        assert(expanded!!.pathIds.equals(pathIds))
    }

    @Test
    fun pathIdsOnRoot() {
        assert(this.root!!.pathIds.size == 1)
        assert(this.root!!.pathIds[0].equals(this.root!!.id))
    }
}
