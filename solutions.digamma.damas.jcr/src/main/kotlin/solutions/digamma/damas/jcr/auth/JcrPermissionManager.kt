package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.auth.PermissionManager
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.content.JcrFile
import solutions.digamma.damas.jcr.content.JcrFolder
import solutions.digamma.damas.jcr.model.JcrManager
import solutions.digamma.damas.logging.Logged
import javax.inject.Singleton

/**
 * JCR implementation of permission management service.
 *
 * @author Ahmad Shahwan
 */
@Singleton
internal open class JcrPermissionManager: JcrManager(), PermissionManager {

    @Logged
    @Throws(WorkspaceException::class)
    override fun retrieve(objectId: String, subjectId: String):
            Permission = Exceptions.check {
        doRetrieve(objectId, subjectId)
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun retrieve(fileId: String): List<Permission> = Exceptions.check {
        doRetrieve(fileId)
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun update(pattern: Permission): Permission = Exceptions.check {
        doUpdate(pattern)
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun update(fileId: String, permissions: List<Permission>) {
        return Exceptions.check {
            doUpdate(fileId, permissions)
        }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun update(
            fileId: String,
            permissions: List<Permission>,
            recursive: Boolean) {
        return Exceptions.check {
            doUpdate(fileId, permissions, recursive)
        }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun delete(objectId: String, subjectId: String) {
        return Exceptions.check {
            doDelete(objectId, subjectId)
        }
    }

    private fun doRetrieve(fileId: String, subjectId: String) =
            JcrPermission.of(this.session, fileId, subjectId)

    private fun doRetrieve(fileId: String) =
            JcrPermission.lisOf(this.session, fileId)

    private fun doUpdate(pattern: Permission): Permission {
        return JcrPermission
                .of(this.session, pattern.objectId, pattern.subjectId).apply {
            this.accessRights = pattern.accessRights
        }
    }

    /**
     * Apply access right changes, in reverse order.
     * IDs of patterns are ignored, as well as their object IDs. The file ID
     * passed as a parameter is used instead as object ID.
     */
    private fun doUpdate(
            fileId: String,
            permissions: List<Permission>) {
        for (pattern in permissions.reversed()) {
            JcrPermission.of(
                    this.session, fileId, pattern.subjectId
            ).accessRights = pattern.accessRights
        }
    }

    /**
     * Apply access right changes, possibly recursively.
     */
    private fun doUpdate(
            fileId: String,
            permissions: List<Permission>,
            recursive: Boolean) {
        doUpdate(fileId, permissions)
        val file = JcrFile.of(this.session.getNodeByIdentifier(fileId))
        if (recursive && file is JcrFolder) {
            file.expandContent(1)
            file.content?.folders?.forEach {
                doUpdate(it.id, permissions, true)
            }
            file.content?.documents?.forEach {
                doUpdate(it.id, permissions, true)
            }
        }
    }

    private fun doDelete(objectId: String, subjectId: String) {
        this.doRetrieve(objectId, subjectId).remove()
    }
}
