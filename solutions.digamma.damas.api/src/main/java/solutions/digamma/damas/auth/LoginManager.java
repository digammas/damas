package solutions.digamma.damas.auth;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.inspection.NotNull;

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
     * @throws DocumentException
     */
    Token login(@NotNull String username, @NotNull String password)
            throws DocumentException;

    void logout(Token token) throws DocumentException;
}
