package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.common.AuthenticationException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.inspection.NotNull

/**
 * Session user.
 *
 * @author Ahmad Shahwan
 */
interface SessionUser {

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
    fun getSession(token: Token): SessionWrapper

    /**
     * Obtain a session wrapper that contains a valid and already open session.
     *
     * @param token     Access token.
     * @return          A valid, non-null open session wrapper.
     * @throws AuthenticationException  When no session is found for the given
     * token.
     * @throws WorkspaceException        When things go wrong while opening
     * session.
     */

    @Throws(WorkspaceException::class)
    fun openSession(token: Token): SessionWrapper {
        return this.getSession(token).open()
    }

}
