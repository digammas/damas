package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.AuthenticationException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jcr.session.JcrTransaction
import solutions.digamma.damas.jcr.session.JcrSessionConsumer
import solutions.digamma.damas.logging.Logged
import java.util.logging.Logger
import javax.inject.Inject

/**
 * Generic manager abstract implementation.
 *
 * @author Ahmad Shahwan
 */
@Logged.Lifecycle
internal abstract class JcrManager : JcrSessionConsumer {

    @Inject
    protected lateinit var logger: Logger

    override val session
    @Logged
    @Throws(AuthenticationException::class)
    get() = try {
        JcrTransaction.get().getSession()
    } catch (e: NotFoundException) {
        throw AuthenticationException(e)
    }
}
