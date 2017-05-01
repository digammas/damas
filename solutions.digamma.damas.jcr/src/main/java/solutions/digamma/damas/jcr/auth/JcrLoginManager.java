package solutions.digamma.damas.jcr.auth;

import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.auth.LoginManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.jcr.fail.JcrExceptionMapper;
import solutions.digamma.damas.jcr.session.SecureToken;
import solutions.digamma.damas.jcr.session.SessionBookkeeper;
import solutions.digamma.damas.jcr.session.UserSession;


import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.*;
import java.util.logging.Logger;

/**
 * JCR login manager. This component accept username/password credentials to
 * authenticate a user and grant them an access token.
 * The token is used to retrieve a stored session each time access is demanded.
 *
 * @author Ahmad Shahwan
 */
@Singleton
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
            this.logger.info("Login successful.");
            SecureToken token = new SecureToken();
            UserSession userSession = new UserSession(jcrSession);
            this.bookkeeper.register(token, userSession);
            this.logger.info("Session registered.");
            return token;
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Override
    public void logout(Token token) throws DocumentException {
        this.bookkeeper.unregister(token);
    }
}
