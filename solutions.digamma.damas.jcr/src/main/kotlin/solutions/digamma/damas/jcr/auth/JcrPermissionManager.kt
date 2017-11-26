package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.auth.PermissionManager
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.content.JcrFile
import solutions.digamma.damas.jcr.content.JcrFolder
import solutions.digamma.damas.jcr.model.JcrManager
import solutions.digamma.damas.logging.Logged
import solutions.digamma.damas.login.Token
import javax.inject.Singleton
import javax.jcr.Session

/**
 * JCR implementation of permission management service.
 *
 * @author Ahmad Shahwan
 */
@Singleton
internal class JcrPermissionManager: JcrManager(), PermissionManager {

    @Logged
    @Throws(WorkspaceException::class)
    override fun retrieve(token: Token, objectId: String, subjectId: String):
            Permission {
        return Exceptions.wrap(openSession(token)) {
            retrieve(it.getSession(), objectId, subjectId)
        }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun retrieve(token: Token, fileId: String): List<Permission> {
        return Exceptions.wrap(openSession(token)) {
            retrieve(it.getSession(), fileId)
        }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun update(token: Token, pattern: Permission): Permission {
        return Exceptions.wrap(openSession(token)) {
            update(it.getSession(), pattern )
        }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun update(
            token: Token,
            fileId: String,
            permissions: List<Permission>) {
        return Exceptions.wrap(openSession(token)) {
            update(it.getSession(), fileId, permissions)
        }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun update(
            token: Token,
            fileId: String,
            permissions: List<Permission>,
            recursive: Boolean) {
        return Exceptions.wrap(openSession(token)) {
            update(it.getSession(), fileId, permissions, recursive)
        }
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun delete(token: Token, objectId: String, subjectId: String) {
        return Exceptions.wrap(openSession(token)) {
            delete(it.getSession(), objectId, subjectId)
        }
    }

    private fun retrieve(session: Session, fileId: String, subjectId: String) =
            JcrPermission.of(session, fileId, subjectId)

    private fun retrieve(session: Session, fileId: String) =
            JcrPermission.lisOf(session, fileId)

    private fun update(session: Session, pattern: Permission): Permission {
        return JcrPermission
                .of(session, pattern.objectId, pattern.subjectId).apply {
            accessRights = pattern.accessRights
        }
    }

    /**
     * Apply access right changes, in reverse order.
     * IDs of patterns are ignored, as well as their object IDs. The file ID
     * passed as a parameter is used instead as object ID.
     */
    private fun update(
            session: Session,
            fileId: String,
            permissions: List<Permission>) {
        for (pattern in permissions.reversed()) {
            JcrPermission.of(session, fileId, pattern.subjectId).accessRights =
                    pattern.accessRights
        }
    }

    /**
     * Apply access right changes, possibly recursively.
     */
    private fun update(
            session: Session,
            fileId: String,
            permissions: List<Permission>,
            recursive: Boolean) {
        update(session, fileId, permissions)
        val file = JcrFile.of(session.getNodeByIdentifier(fileId))
        if (recursive && file is JcrFolder) {
            file.expandContent(1)
            file.content?.folders?.forEach {
                update(session, it.id, permissions, true)
            }
            file.content?.documents?.forEach {
                update(session, it.id, permissions, true)
            }
        }
    }

    private fun delete(session: Session, objectId: String, subjectId: String) {
        this.retrieve(session, objectId, subjectId).remove()
    }
}
