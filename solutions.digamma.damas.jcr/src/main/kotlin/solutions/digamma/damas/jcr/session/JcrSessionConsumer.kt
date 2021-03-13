package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.common.AuthenticationException
import javax.jcr.Session

/**
 * This interface is inherited by services that use (consume) a user session.
 *
 * @author Ahmad Shahwan
 */
internal interface JcrSessionConsumer {

    /**
     * Obtain a valid session from current transaction.
     *
     * @return          A valid, non-null JCR session.
     * @throws AuthenticationException  When no session is found for the given
     * token.
     */
    @get:Throws(AuthenticationException::class)
    val session: Session
}
