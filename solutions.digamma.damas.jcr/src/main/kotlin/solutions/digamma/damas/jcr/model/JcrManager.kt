package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.common.AuthenticationException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jcr.session.SessionBookkeeper
import solutions.digamma.damas.jcr.session.SessionUser
import solutions.digamma.damas.jcr.session.SessionWrapper
import java.util.logging.Logger
import javax.inject.Inject

/**
 * Generic manager abstract implementation.
 *
 * @author Ahmad Shahwan
 */
abstract class JcrManager : SessionUser {

    @Inject
    protected var logger: Logger? = null

    @Inject
    private var bookkeeper: SessionBookkeeper? = null

    @Throws(AuthenticationException::class)
    override fun getSession(token: Token): SessionWrapper {
        try {
            return this.bookkeeper!!.lookup(token)
        } catch (e: NotFoundException) {
            throw AuthenticationException("No session for token.", e)
        }

    }
}
