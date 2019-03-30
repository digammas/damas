package solutions.digamma.damas.jcr.login

import solutions.digamma.damas.common.AuthenticationException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.session.SessionBookkeeper
import solutions.digamma.damas.login.Authentication
import solutions.digamma.damas.login.AuthenticationManager
import solutions.digamma.damas.login.Token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserAuthenticationManager : AuthenticationManager {

    @Inject
    private lateinit var bookkeepr: SessionBookkeeper

    @Throws(WorkspaceException::class)
    override fun authenticate(token: Token?): Authentication {
        try {
            return UserAuthentication(this.bookkeepr.lookup(token))
        } catch (e: NotFoundException) {
            throw AuthenticationException(e)
        }
    }
}