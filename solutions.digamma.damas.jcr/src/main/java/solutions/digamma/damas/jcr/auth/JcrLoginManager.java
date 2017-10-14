package solutions.digamma.damas.jcr.auth;

import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.auth.LoginManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.jcr.error.JcrException;
import solutions.digamma.damas.jcr.session.SecureToken;
import solutions.digamma.damas.jcr.session.SessionBookkeeper;
import solutions.digamma.damas.jcr.session.SessionWrapper;


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
    public Token login(@NotNull String username, @NotNull String password)
            throws WorkspaceException {
        try {
            Credentials credentials = new SimpleCredentials(
                    username, password.toCharArray());
            Session jcrSession = this.repository.login(credentials);
            this.logger.info("Login successful.");
            SecureToken token = new SecureToken();
            SessionWrapper session = new SessionWrapper(jcrSession);
            this.bookkeeper.register(token, session);
            this.logger.info("Session registered.");
            return token;
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    @Override
    public void logout(Token token) throws WorkspaceException {
        this.bookkeeper.unregister(token);
    }
}
