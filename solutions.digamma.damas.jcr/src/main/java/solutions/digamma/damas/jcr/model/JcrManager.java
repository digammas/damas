package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.common.AuthenticationException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.jcr.session.SessionUser;
import solutions.digamma.damas.jcr.session.SessionBookkeeper;
import solutions.digamma.damas.jcr.session.SessionWrapper;
import solutions.digamma.damas.inspection.NotNull;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Generic manager abstract implementation.
 *
 * @author Ahmad Shahwan
 */
abstract public class JcrManager implements SessionUser {

    @Inject
    protected Logger logger;

    @Inject
    private SessionBookkeeper bookkeeper;

    @Override
    public @NotNull SessionWrapper getSession(@NotNull Token token)
            throws AuthenticationException {
        try {
            return this.bookkeeper.lookup(token);
        } catch (NotFoundException e) {
            throw new AuthenticationException("No session for token.", e);
        }
    }
}
