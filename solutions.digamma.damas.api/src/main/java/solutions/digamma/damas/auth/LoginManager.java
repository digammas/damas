package solutions.digamma.damas.auth;

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
     * @param username
     * @param password
     * @return
     * @throws WorkspaceException
     */
    Token login(String username, String password)
            throws WorkspaceException;

    void logout(Token token) throws WorkspaceException;
}
