package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.inspection.NotNull
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.auth.LoginManager
import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.session.SecureToken
import solutions.digamma.damas.jcr.session.SessionBookkeeper
import solutions.digamma.damas.jcr.session.SessionWrapper


import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.*
import java.util.logging.Logger

/**
 * JCR login manager. This component accept username/password credentials to
 * authenticate a user and grant them an access token.
 * The token is used to retrieve a stored session each time access is demanded.
 *
 * @author Ahmad Shahwan
 */
@Singleton
class JcrLoginManager : LoginManager {

    @Inject
    private var logger: Logger? = null

    @Inject
    private var repository: Repository? = null

    @Inject
    private var bookkeeper: SessionBookkeeper? = null

    @Throws(WorkspaceException::class)
    override fun login(username: String, password: String): Token {
        try {
            val credentials = SimpleCredentials(
                    username, password.toCharArray())
            val jcrSession = this.repository!!.login(credentials)
            this.logger?.info("Login successful.")
            val token = SecureToken()
            val session = SessionWrapper(jcrSession)
            this.bookkeeper!!.register(token, session)
            this.logger?.info("Session registered.")
            return token
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

    }

    @Throws(WorkspaceException::class)
    override fun logout(token: Token) {
        this.bookkeeper!!.unregister(token)
    }
}
