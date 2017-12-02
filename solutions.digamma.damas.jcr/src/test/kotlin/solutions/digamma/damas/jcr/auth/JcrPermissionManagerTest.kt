package solutions.digamma.damas.jcr.auth

import org.junit.After
import org.junit.Before
import org.junit.Test
import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.DocumentManager
import solutions.digamma.damas.content.FolderManager
import solutions.digamma.damas.jcr.Mocks
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.user.UserManager
import java.util.EnumSet

class JcrPermissionManagerTest   : WeldTest() {

    private val userManager = WeldTest.inject(UserManager::class.java)
    private val folderManager = WeldTest.inject(FolderManager::class.java)
    private val documentManager = WeldTest.inject(DocumentManager::class.java)
    private val manager = WeldTest.inject(JcrPermissionManager::class.java)
    private lateinit var token: Token
    private lateinit var username: String
    private lateinit var userToken: Token
    private lateinit var folderId: String
    private lateinit var subfolderId: String
    private lateinit var docId: String

    @Before
    fun setUp() {
        this.token = this.login.login("admin", "admin")
        val user = Mocks.user("tester")
        this.username = this.userManager.create(this.token, user).login
        val password = "P@55w0rd"
        this.userManager.updatePassword(this.token, this.username, password)
        this.userToken = this.login.login(this.username, password)

        val rootId = this.folderManager
                .find(this.token).objects.iterator().next().id
        this.folderId = folderManager.create(
                this.token, Mocks.folder(rootId, "folder")).id
        this.subfolderId = folderManager.create(
                this.token, Mocks.folder(folderId, "subfolder")).id
        this.docId = documentManager.create(
                this.token, Mocks.document(folderId, "doc-um.ent")).id
    }

    @After
    fun tearDown() {
        this.folderManager.delete(this.token, this.folderId)
        this.login.logout(this.userToken)
        this.userManager.delete(this.token, this.username)
        this.login.logout(this.token)
    }


    @Test
    fun retrieve() {
        val permission = this.manager.retrieve(this.token, folderId, username)
        assert(permission.accessRights.isEmpty())
    }

    @Test
    fun update() {
        try {
            folderManager.retrieve(userToken, folderId)
            assert(false) { "Expecting access rights denial." }
        } catch (_: WorkspaceException) {}
        val pattern = Mocks.permission(
                folderId, username, EnumSet.of(AccessRight.READ))
        val permission = manager.update(token, pattern)
        assert(permission.accessRights == EnumSet.of(AccessRight.READ))
        manager.delete(token, folderId, username)
    }

    @Test
    fun delete() {
    }

}