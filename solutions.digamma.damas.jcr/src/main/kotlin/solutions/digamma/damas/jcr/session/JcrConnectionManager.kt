package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.common.AuthenticationException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.session.Connection
import solutions.digamma.damas.session.ConnectionManager
import solutions.digamma.damas.session.Token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal open class JcrConnectionManager : ConnectionManager {

    @Inject
    private lateinit var bookkeepr: SessionBookkeeper

    @Throws(WorkspaceException::class)
    override fun connect(token: Token): Connection {
        try {
            return JcrConnection(this.bookkeepr.lookup(token))
        } catch (e: NotFoundException) {
            throw AuthenticationException(e)
        }
    }
}
