package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.auth.PermissionManager
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.login.Token
import javax.jcr.Session

/**
 * JCR implementation of permission management service.
 *
 * @author Ahmad Shahwan
 */
internal class JcrPermissionManager:
        JcrCrudManager<Permission>(), PermissionManager {

    @Throws(WorkspaceException::class)
    override fun retrieve(token: Token, fileId: String, subjectId: String):
            Permission {
        return Exceptions.wrap(openSession(token)) {
            retrieve(it.getSession(), fileId, subjectId)
        }
    }

    @Throws(WorkspaceException::class)
    override fun retrieveAt(token: Token, fileId: String): List<Permission> {
        return Exceptions.wrap(openSession(token)) {
            retrieveAt(it.getSession(), fileId)
        }
    }

    @Throws(WorkspaceException::class)
    override fun updateAt(
            token: Token,
            fileId: String,
            permissions: List<Permission>) {
        return Exceptions.wrap(openSession(token)) {
            updateAt(it.getSession(), fileId, permissions)
        }
    }

    override fun update(session: Session, id: String, pattern: Permission) =
            this.retrieve(session, id).also {
        it.accessRights = pattern.accessRights
    }

    override fun delete(session: Session, id: String) {
        this.retrieve(session, id).remove()
    }

    override fun retrieve(session: Session, id: String) =
        JcrPermission.of(session, id)

    override fun create(session: Session, pattern: Permission): Permission {
        return JcrPermission
                .of(session, pattern.objectId, pattern.subjectId).also {
            it.accessRights = pattern.accessRights
        }
    }

    fun retrieve(session: Session, fileId: String, subjectId: String) =
            JcrPermission.of(session, fileId, subjectId)

    fun retrieveAt(session: Session, fileId: String) =
        JcrPermission.lisOf(session, fileId)

    /**
     * Apply access right changes, in reverse order.
     * IDs of patterns are ignored, as well as their object IDs. The file ID
     * passed as a parameter is used instead as object ID.
     */
    fun updateAt(
            session: Session,
            fileId: String,
            permissions: List<Permission>) {
        for (pattern in permissions.reversed()) {
            JcrPermission.of(session, fileId, pattern.subjectId).accessRights =
                    pattern.accessRights
        }
    }
}
