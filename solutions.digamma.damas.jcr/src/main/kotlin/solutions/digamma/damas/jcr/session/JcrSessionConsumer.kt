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
     * Obtain a session wrapper that contains a valid session. Returned session
     * need not be open.
     *
     * @param token     Access token.
     * @return          A valid, non-null session wrapper.
     * @throws AuthenticationException  When no session is found for the given
     * token.
     */
    @Throws(AuthenticationException::class)
    fun getSession(): Session
}
