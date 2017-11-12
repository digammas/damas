package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.auth.PermissionManager
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.login.Token
import javax.jcr.Session

/**
 * @author Ahmad Shahwan
 */
internal class JcrPermissionManager:
        JcrCrudManager<Permission>(), PermissionManager {

    override fun update(session: Session, id: String, pattern: Permission): Permission {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(session: Session, id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieve(session: Session, id: String): Permission {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(session: Session, pattern: Permission): Permission {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieve(token: Token?, fileId: String?, subjectId: String?): Permission {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveAt(token: Token?, fileId: String?): MutableList<Permission> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAt(token: Token?, fileId: String?, permissions: MutableList<Permission>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
