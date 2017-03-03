package solutions.digamma.damas.jcr.auth;

import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.auth.LoginManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.jcr.fail.JcrException;


import javax.inject.Inject;
import javax.jcr.*;
import java.util.logging.Logger;

/**
 * @author Ahmad Shahwan
 */
public class JcrLoginManager implements LoginManager {

    @Inject
    private Logger logger;

    @Inject
    private Repository repository;

    @Inject
    private SessionBookkeeper bookkeeper;

    @Override
    public Token login(@Nonnull String username, @Nonnull String password)
            throws DocumentException {
        try {
            Credentials credentials = new SimpleCredentials(
                    username, password.toCharArray());
            Session jcrSession = this.repository.login(credentials);
            SecureToken token = new SecureToken();
            UserSession userSession = new UserSession(jcrSession);
            this.bookkeeper.register(token, userSession);
            return token;
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    @Override
    public void logout(Token token) throws DocumentException {
        this.bookkeeper.unregister(token);
    }
}
