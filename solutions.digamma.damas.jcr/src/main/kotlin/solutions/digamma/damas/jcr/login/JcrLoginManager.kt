package solutions.digamma.damas.jcr.login


import solutions.digamma.damas.login.LoginManager
import solutions.digamma.damas.login.Token
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.session.SecureToken
import solutions.digamma.damas.jcr.session.SessionBookkeeper
import solutions.digamma.damas.jcr.session.SessionWrapper
import solutions.digamma.damas.logging.Logged
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.Repository
import javax.jcr.SimpleCredentials

/**
 * JCR login manager. This component accept username/password credentials to
 * authenticate a user and grant them an access token.
 * The token is used to retrieve a stored session each time access is demanded.
 *
 * @author Ahmad Shahwan
 */
@Singleton
internal class JcrLoginManager : LoginManager {

    @Inject
    private lateinit var logger: Logger

    @Inject
    private lateinit var repository: Repository

    @Inject
    private lateinit var bookkeeper: SessionBookkeeper

    @Logged
    @Throws(WorkspaceException::class)
    override fun login(username: String, password: String) = Exceptions.wrap {
        val credentials = SimpleCredentials(
                username, password.toCharArray())
        val jcrSession = this.repository.login(credentials)
        this.logger.info("Login successful.")
        val token = SecureToken()
        val session = SessionWrapper(jcrSession)
        this.bookkeeper.register(token, session)
        this.logger.info("Session registered.")
        token
    }

    @Logged
    @Throws(WorkspaceException::class)
    override fun logout(token: Token) = this.bookkeeper.unregister(token)
}
