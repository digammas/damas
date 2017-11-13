package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.auth.PermissionManager
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.login.Token
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * @author Ahmad Shahwan
 */
internal class JcrPermissionManager:
        JcrCrudManager<Permission>(), PermissionManager {

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun update(session: Session, id: String, pattern: Permission) =
            this.retrieve(session, id).also {
        it.accessRights = pattern.accessRights
    }

    @Throws(WorkspaceException::class)
    override fun delete(session: Session, id: String) {
        this.retrieve(session, id).remove()
    }

    @Throws(WorkspaceException::class)
    override fun retrieve(session: Session, id: String) =
        JcrPermission.of(session, id)

    @Throws(WorkspaceException::class)
    override fun create(session: Session, pattern: Permission): Permission {
        return JcrPermission
                .of(session, pattern.objectId, pattern.subjectId).also {
            it.accessRights = pattern.accessRights
        }
    }

    @Throws(WorkspaceException::class)
    override fun retrieve(token: Token, fileId: String, subjectId: String):
            Permission {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveAt(token: Token?, fileId: String?): MutableList<Permission> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAt(token: Token?, fileId: String?, permissions: MutableList<Permission>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
