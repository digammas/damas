package solutions.digamma.damas.jcr.test

import org.jboss.weld.environment.se.Weld
import org.jboss.weld.environment.se.WeldContainer
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.Mockito
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.entity.Page
import solutions.digamma.damas.auth.LoginManager
import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.DocumentManager
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.content.FolderManager

import java.io.IOException

/**
 * Test content managers.
 *
 * @author Ahmad Shahwan
 */
class ContentTest {

    private var loginMgr: LoginManager? = null
    private var documentMgr: DocumentManager? = null
    private var folderMgr: FolderManager? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        this.loginMgr = container!!.select(LoginManager::class.java).get()
        this.documentMgr = container!!.select(DocumentManager::class.java).get()
        this.folderMgr = container!!.select(FolderManager::class.java).get()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
    }

    @Throws(WorkspaceException::class)
    private fun createDocument(token: Token, parentId: String, name: String): Document {
        val document = Mockito.mock(Document::class.java)
        Mockito.`when`(document.parentId).thenReturn(parentId)
        Mockito.`when`(document.name).thenReturn(name)
        return this.documentMgr!!.create(token, document)
    }

    @Test
    @Throws(WorkspaceException::class)
    fun testContent() {
        val adminToken = loginMgr!!.login("admin", "admin")!!
        val folderResult = this.folderMgr!!.find(adminToken)
        val rootFolder = folderResult.objects[0]
        val testFileName = "test.txt"
        var doc: Document?
        doc = this.createDocument(
                adminToken,
                rootFolder.id,
                testFileName
        )
        assert(doc != null)
        val docId = doc.id
        doc = this.documentMgr!!.retrieve(adminToken, docId)
        assert(testFileName == doc!!.name)
        this.documentMgr!!.delete(adminToken, docId)
        try {
            this.documentMgr!!.retrieve(adminToken, docId)
            assert(false)
        } catch (e: NotFoundException) {
            assert(true)
        }

    }

    companion object {

        private var container: WeldContainer? = null

        @JvmStatic
        @BeforeClass
        fun setUpClass() {
            container = Weld().initialize()
        }

        @JvmStatic
        @AfterClass
        fun tearDownClass() {
            container!!.close()
        }
    }
}
