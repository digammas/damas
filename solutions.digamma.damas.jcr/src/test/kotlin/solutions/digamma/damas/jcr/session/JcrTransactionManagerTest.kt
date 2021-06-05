package solutions.digamma.damas.jcr.session

import org.junit.After
import org.junit.Before
import org.junit.Test
import solutions.digamma.damas.common.AuthenticationException
import solutions.digamma.damas.content.FolderManager
import solutions.digamma.damas.jcr.Mocks
import solutions.digamma.damas.jcr.RepositoryTest

class JcrTransactionManagerTest : RepositoryTest() {

    private var folderManager = inject(FolderManager::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    fun beginAuthorised() {
        val token = this.login.login("admin", "admin")
        authenticator.connect(token).use {
            val rootId = this.folderManager.find("/").id
            val folder = folderManager.create(Mocks.folder(rootId, "test"))
            assert(rootId == folder.parentId) { "Folder ID mismatch" }
            folderManager.delete(folder.id)
        }
    }

    @Test
    fun beginUnauthorisedBefore() {
        try {
            this.folderManager.find("/").id
            assert(false) { "Unauthorized operation before authentication" }
        } catch (e: AuthenticationException) {
            assert(true) { "Wrong exception for unauthorized operation" }
        }
    }

    @Test
    fun beginUnauthorisedAfter() {
        val token = this.login.login("admin", "admin")
        authenticator.connect(token).use {
            this.folderManager.find("/").id
        }
        try {
            this.folderManager.find("/").id
            assert(false) { "Unauthorized operation after authentication" }
        } catch (e: AuthenticationException) {
            assert(true) { "Wrong exception for unauthorized operation" }
        }
    }
}
