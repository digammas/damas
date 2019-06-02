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
import solutions.digamma.damas.user.GroupManager
import solutions.digamma.damas.user.UserManager
import java.util.Arrays

class JcrPermissionManagerTest: WeldTest() {

    private val userManager = WeldTest.inject(UserManager::class.java)
    private val groupManager = WeldTest.inject(GroupManager::class.java)
    private val folderManager = WeldTest.inject(FolderManager::class.java)
    private val documentManager = WeldTest.inject(DocumentManager::class.java)
    private val manager = WeldTest.inject(JcrPermissionManager::class.java)
    private lateinit var username: String
    private lateinit var userToken: Token
    private lateinit var folderId: String
    private lateinit var subfolderId: String
    private lateinit var docId: String
    private lateinit var groupId: String

    @Before
    fun setUp() {
        this.login()
        val group = Mocks.group("testers")
        this.groupId = this.groupManager.create(group).id
        val user = Mocks.user("tester", Arrays.asList(this.groupId))
        this.username = this.userManager.create(user).login
        val password = "P@55w0rd"
        this.userManager.updatePassword(this.username, password)
        this.commit()

        this.userToken = this.login.login(this.username, password)

        val rootId = this.folderManager
                .find("/").id
        this.folderId = folderManager.create(
                Mocks.folder(rootId, "folder")).id
        this.subfolderId = folderManager.create(
                Mocks.folder(folderId, "subfolder")).id
        this.docId = documentManager.create(
                Mocks.document(folderId, "doc-um.ent")).id
    }

    @After
    fun tearDown() {
        this.manager.delete(this.folderId, this.username)
        this.folderManager.delete(this.folderId)
        this.login.logout(this.userToken)
        this.userManager.delete(this.username)
        this.groupManager.delete(this.groupId)
        this.logout()
    }


    @Test
    fun retrieve() {
        val permission = this.manager.retrieve(folderId, username)
        assert(permission.accessRights == AccessRight.NONE) {
            "Error retrieving permission."
        }
    }

    @Test
    fun update() {
        this.use(this.userToken){
            folderManager.retrieve(folderId)
            assert(false) { "Expecting access rights denial." }
        }
        assert(manager.update(
            Mocks.permission(folderId, username, AccessRight.READ)
        ).accessRights == AccessRight.READ) {
            "Error creating permission."
        }
        this.use(this.userToken) {
            folderManager.retrieve(folderId)
            folderManager.update(folderId, Mocks.folder(null, "new_name"))
            assert(false) { "Expecting access rights denial." }
        }
        assert(manager.update(
                Mocks.permission(folderId, username, AccessRight.WRITE)
        ).accessRights == AccessRight.WRITE) { "Error updating permission." }
        this.use(this.userToken) {
            folderManager.update(folderId, Mocks.folder(null, "new_name"))
        }
    }

    @Test
    fun updateForGroup() {
        this.use(this.userToken) {
            folderManager.retrieve(folderId)
            assert(false) { "Expecting access rights denial." }
        }
        assert(manager.update(
                Mocks.permission(folderId, groupId, AccessRight.READ)
        ).accessRights == AccessRight.READ) { "Error creating permission." }
        this.use(this.userToken) {
            folderManager.retrieve(folderId)

            folderManager.update(folderId, Mocks.folder(null, "new_name"))
            assert(false) { "Expecting access rights denial." }
        }
        assert(manager.update(
                Mocks.permission(folderId, groupId, AccessRight.WRITE)
        ).accessRights == AccessRight.WRITE) {
            "Error updating permission."
        }
        this.use(this.userToken) {
            folderManager.update(folderId, Mocks.folder(null, "new_name"))
        }
    }

    @Test
    fun updateRecursive() {
        this.use(this.userToken) {
            folderManager.retrieve(subfolderId)
            assert(false) { "Expecting access rights denial." }
        }
        /* Try with no recursion */
        manager.update(folderId, Arrays.asList(
                Mocks.permission(null, username, AccessRight.READ)))
        /* It won't work */
        this.use(this.userToken) {
            folderManager.retrieve(subfolderId)
            assert(false) { "Expecting access rights denial." }
        }
        /* Grand access on folder, recursively */
        manager.update(folderId, Arrays.asList(
                Mocks.permission(null, username, AccessRight.READ)), true)
        this.use(this.userToken) {
            /* Also works on sub-folder */
            folderManager.retrieve(subfolderId)
            /* Not for write, yet */
            folderManager.update(subfolderId, Mocks.folder(null, "new_name")
            )
            assert(false) { "Expecting access rights denial." }
        }
        /* Grant write access, recursively */
        manager.update(folderId, Arrays.asList(
                Mocks.permission(null, username, AccessRight.WRITE)), true)
        this.use(this.userToken) {
            /* And watch it apply to children */
            folderManager.update(folderId, Mocks.folder(null, "new_name"))
        }
        /* Revoke all */
        manager.update(folderId, Arrays.asList(
                Mocks.permission(null, username, AccessRight.NONE)), true)
        this.use(this.userToken) {
            /* Back to square one */
            folderManager.retrieve(subfolderId)
            assert(false) { "Expecting access rights denial." }
        }
    }

    @Test
    fun delete() {
        manager.update(Mocks.permission(folderId, username, AccessRight.READ))
        this.use(this.userToken) {
            folderManager.retrieve(folderId)
        }
        manager.delete(folderId, username)
        this.use(this.userToken) {
            folderManager.retrieve(folderId)
            assert(false) { "Expecting access rights denial." }
        }
    }

    private fun <R> use(token: Token, block: () -> R): R? {
        this.authenticator.begin(token).use {
            try {
                return block()
            } catch (_: WorkspaceException) {
                return null
            }
        }
    }
}