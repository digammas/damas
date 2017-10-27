package solutions.digamma.damas.jcr.content

import org.junit.After
import org.junit.Before
import org.junit.Test
import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.content.FolderManager
import solutions.digamma.damas.jcr.Mocks
import solutions.digamma.damas.jcr.WeldTest

/**
 * Test folder object.
 *
 * Created by Ahmad on 9/3/17.
 */
class JcrFolderTest : WeldTest() {

    private var token: Token? = null
    private var folder: Folder? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val manager = WeldTest.inject(JcrFolderManager::class.java)
        this.token = this.login!!.login("admin", "admin")
        var parentId = manager
                .find(this.token!!).objects.iterator().next().id
        this.folder = manager.create(this.token!!,
                Mocks.folder(parentId, "test"))
        var parent = this.folder
        for (i in 0..9) {
            parentId = parent!!.id
            val name = "test_$i"
            parent = manager.create(this.token!!,
                    Mocks.folder(parentId, name))
        }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        this.login!!.logout(this.token)
    }

    @Test
    @Throws(Exception::class)
    fun expand() {
        this.folder!!.expand()

    }

    @Test
    @Throws(Exception::class)
    fun expandContent() {
        var expanded = this.folder
        expanded!!.expandContent()
        for (i in 0..9) {
            expanded = expanded!!.content.folders.iterator().next()
        }
    }

    @Test
    @Throws(Exception::class)
    fun expandContentToDepth() {
        var expanded = this.folder
        val depth = 5
        expanded!!.expandContent(depth)
        for (i in 0 until depth) {
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
}