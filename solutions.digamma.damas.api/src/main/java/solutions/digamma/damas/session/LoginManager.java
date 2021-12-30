package solutions.digamma.damas.session;

import solutions.digamma.damas.common.WorkspaceException;

/**
 * Username/password login manager.
 *
 * @author Ahmad Shahwan
 */
public interface LoginManager {

    /**
     * Check user credentials and grant them a user session if credentials are
     * valid. If credential are not usable, throw exception.
     *
     * @param username  username
     * @param password  password
     * @return          a token representing user session
     * @throws WorkspaceException   when credential are not valid, or another
     *                              error occurres
     */
    UserToken login(String username, String password) throws WorkspaceException;

    UserSession identify(Token token) throws WorkspaceException;

    /**
     * Log out previously logged user, using their token.
     *
     * @param token     user token
     * @throws WorkspaceException   if an error occurres
     */
    void logout(Token token) throws WorkspaceException;
}
