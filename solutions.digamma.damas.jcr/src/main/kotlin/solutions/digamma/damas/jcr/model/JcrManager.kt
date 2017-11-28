package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.login.Token
import solutions.digamma.damas.common.AuthenticationException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jcr.session.SessionBookkeeper
import solutions.digamma.damas.jcr.session.SessionConsumer
import java.util.logging.Logger
import javax.inject.Inject

/**
 * Generic manager abstract implementation.
 *
 * @author Ahmad Shahwan
 */
internal abstract class JcrManager : SessionConsumer {

    @Inject
    protected lateinit var logger: Logger

    @Inject
    private lateinit var bookkeeper: SessionBookkeeper

    @Throws(AuthenticationException::class)
    override fun getSession(token: Token) = try {
        this.bookkeeper.lookup(token)
    } catch (e: NotFoundException) {
        throw AuthenticationException("No session for token.", e)
    }
}
