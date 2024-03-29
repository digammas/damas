package solutions.digamma.damas.jcr.login


import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.session.LoginManager
import solutions.digamma.damas.session.Token
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.session.SecureToken
import solutions.digamma.damas.jcr.session.SessionBookkeeper
import solutions.digamma.damas.jcr.session.TransactionalSession
import solutions.digamma.damas.logging.Logged
import solutions.digamma.damas.session.UserSession
import solutions.digamma.damas.session.UserToken
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
internal open class JcrLoginManager : LoginManager {

    @Inject
    private lateinit var logger: Logger

    @Inject
    private lateinit var repository: Repository

    @Inject
    private lateinit var bookkeeper: SessionBookkeeper

    @Logged
    @Throws(WorkspaceException::class)
    override fun login(username: String, password: String) = Exceptions.check {
        val credentials = SimpleCredentials(
                username, password.toCharArray())
        val jcrSession = this.repository.login(credentials)
        this.logger.info("Login successful.")
        val token = SecureToken(username)
        val session = TransactionalSession(jcrSession, username)
        this.bookkeeper.register(token, session)
        this.logger.info("Session registered.")
        token
    }

    @Throws(NotFoundException::class)
    override fun identify(token: Token) = this.bookkeeper.lookup(token)

    @Logged
    @Throws(WorkspaceException::class)
    override fun logout(token: Token) = this.bookkeeper.unregister(token)
}
