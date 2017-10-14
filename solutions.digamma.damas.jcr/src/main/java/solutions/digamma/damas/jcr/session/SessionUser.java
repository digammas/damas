package solutions.digamma.damas.jcr.session;

import solutions.digamma.damas.AuthenticationException;
import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;

/**
 * Session user.
 *
 * @author Ahmad Shahwan
 */
public interface SessionUser {

    /**
     * Obtain a session wrapper that contains a valid session. Returned session
     * need not be open.
     *
     * @param token     Access token.
     * @return          A valid, non-null session wrapper.
     * @throws AuthenticationException  When no session is found for the given
     * token.
     */
    @NotNull
    SessionWrapper getSession(@NotNull Token token)
            throws AuthenticationException;

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
    @NotNull
    default SessionWrapper openSession(@NotNull Token token)
            throws WorkspaceException {
        return this.getSession(token).open();
    }

}
