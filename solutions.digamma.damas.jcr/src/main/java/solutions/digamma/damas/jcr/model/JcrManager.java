package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.AuthenticationException;
import solutions.digamma.damas.InternalStateException;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.jcr.repo.RepositoryInitializer;
import solutions.digamma.damas.jcr.session.SessionBookkeeper;
import solutions.digamma.damas.jcr.session.UserSession;
import solutions.digamma.damas.inspection.Nonnull;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Basic generic manager.
 *
 * @author Ahmad Shahwan
 */
public class JcrManager {

    private static final long INIT_TIMEOUT_SEC = 30;

    @Inject
    protected Logger logger;

    @Inject
    private SessionBookkeeper bookkeeper;

    @Inject
    private RepositoryInitializer initializer;

    @Nonnull
    protected UserSession getSession(@Nonnull Token token)
            throws AuthenticationException {
        try {
            return this.bookkeeper.lookup(token);
        } catch (NotFoundException e) {
            throw new AuthenticationException("No session for token.", e);
        }
    }

    /**
     * Wait for repository initialization.
     *
     * @throws InternalStateException
     */
    protected void waitForInitialization() throws InternalStateException {
        try {
            if (!this.initializer.waitUntilDone(INIT_TIMEOUT_SEC)) {
                throw new InternalStateException("Repository not yet ready.");
            }
        } catch (InterruptedException e) {
            throw new InternalStateException(e);
        }
    }
}
