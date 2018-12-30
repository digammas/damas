package solutions.digamma.damas.jcr.auth

import org.junit.After
import org.junit.Before
import org.junit.Ignore
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
import java.util.Collections

class JcrPermissionManagerTest: WeldTest() {

    private val userManager = WeldTest.inject(UserManager::class.java)
    private val groupManager = WeldTest.inject(GroupManager::class.java)
    private val folderManager = WeldTest.inject(FolderManager::class.java)
    private val documentManager = WeldTest.inject(DocumentManager::class.java)
    private val manager = WeldTest.inject(JcrPermissionManager::class.java)
    private lateinit var token: Token
    private lateinit var username: String
    private lateinit var userToken: Token
    private lateinit var folderId: String
    private lateinit var subfolderId: String
    private lateinit var docId: String
    private lateinit var groupId: String

    @Before
    fun setUp() {
        this.token = this.login.login("admin", "admin")
        val group = Mocks.group("testers")
        this.groupId = this.groupManager.create(this.token, group).id
        val user = Mocks.user("tester", Arrays.asList(this.groupId))
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
        this.manager.delete(this.token, this.folderId, this.username)
        this.folderManager.delete(this.token, this.folderId)
        this.login.logout(this.userToken)
        this.userManager.delete(this.token, this.username)
        this.groupManager.delete(this.token, this.groupId)
        this.login.logout(this.token)
    }


    @Test
    fun retrieve() {
        val permission = this.manager.retrieve(this.token, folderId, username)
        assert(permission.accessRights == AccessRight.NONE) {
            "Error retrieving permission."
        }
    }

    @Test
    fun update() {
        try {
            folderManager.retrieve(userToken, folderId)
            assert(false) { "Expecting access rights denial." }
        } catch (_: WorkspaceException) {}
        assert(manager.update(token,
            Mocks.permission(folderId, username, AccessRight.READ)
        ).accessRights == AccessRight.READ) {
            "Error creating permission."
        }
        folderManager.retrieve(userToken, folderId)
        try {
            folderManager.update(userToken, folderId,
                    Mocks.folder(null, "new_name")
            )
            assert(false) { "Expecting access rights denial." }
        } catch (_: WorkspaceException) {}
        assert(manager.update(token,
                Mocks.permission(folderId, username, AccessRight.WRITE)
        ).accessRights == AccessRight.WRITE) {
            "Error updating permission."
        }
        folderManager.update(userToken, folderId,
            Mocks.folder(null, "new_name")
        )
    }

    @Test
    fun updateForGroup() {
        try {
            folderManager.retrieve(userToken, folderId)
            assert(false) { "Expecting access rights denial." }
        } catch (_: WorkspaceException) {}
        assert(manager.update(token,
                Mocks.permission(folderId, groupId, AccessRight.READ)
        ).accessRights == AccessRight.READ) {
            "Error creating permission."
        }
        folderManager.retrieve(userToken, folderId)
        try {
            folderManager.update(userToken, folderId,
                    Mocks.folder(null, "new_name")
            )
            assert(false) { "Expecting access rights denial." }
        } catch (_: WorkspaceException) {}
        assert(manager.update(token,
                Mocks.permission(folderId, groupId, AccessRight.WRITE)
        ).accessRights == AccessRight.WRITE) {
            "Error updating permission."
        }
        folderManager.update(userToken, folderId,
                Mocks.folder(null, "new_name")
        )
    }

    @Test
    fun updateRecursive() {
        try {
            folderManager.retrieve(userToken, subfolderId)
            assert(false) { "Expecting access rights denial." }
        } catch (_: WorkspaceException) {}
        /* Try with no recursion */
        manager.update(token, folderId, Arrays.asList(
                Mocks.permission(null, username, AccessRight.READ)))
        /* It won't work */
        try {
            folderManager.retrieve(userToken, subfolderId)
            assert(false) { "Expecting access rights denial." }
        } catch (_: WorkspaceException) {}
        /* Grand access on folder, recursively */
        manager.update(token, folderId, Arrays.asList(
                Mocks.permission(null, username, AccessRight.READ)), true)
        /* Also works on sub-folder */
        folderManager.retrieve(userToken, subfolderId)
        /* Not for write, yet */
        try {
            folderManager.update(userToken, subfolderId,
                    Mocks.folder(null, "new_name")
            )
            assert(false) { "Expecting access rights denial." }
        } catch (_: WorkspaceException) {}
        /* Grant write access, recursively */
        manager.update(token, folderId, Arrays.asList(
                Mocks.permission(null, username, AccessRight.WRITE)), true)
        /* And watch it apply to children */
        folderManager.update(userToken, folderId,
                Mocks.folder(null, "new_name")
        )
        /* Revoke all */
        manager.update(token, folderId, Arrays.asList(
                Mocks.permission(null, username, AccessRight.NONE)), true)
        /* Back to square one */
        try {
            folderManager.retrieve(userToken, subfolderId)
            assert(false) { "Expecting access rights denial." }
        } catch (_: WorkspaceException) {}
    }

    @Test
    fun delete() {
        manager.update(token,
                Mocks.permission(folderId, username, AccessRight.READ))
        folderManager.retrieve(userToken, folderId)
        manager.delete(token, folderId, username)
        try {
            folderManager.retrieve(userToken, folderId)
            assert(false) { "Expecting access rights denial." }
        } catch (_: WorkspaceException) {}
    }
}