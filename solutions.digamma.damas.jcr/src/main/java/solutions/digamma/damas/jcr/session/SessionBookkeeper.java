package solutions.digamma.damas.jcr.session;

import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.ConflictException;
import solutions.digamma.damas.NotFoundException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Ahmad Shahwan
 */
@Singleton
public class SessionBookkeeper {

    @Inject
    private Logger logger;

    private final Map<String, UserSession> sessions;

    public SessionBookkeeper() {
        this.sessions = new HashMap<>();
    }

    /**
     * Register a given session under the given token. If a session was
     * registered with the same token, a {@code {@link ConflictException}} will
     * be thrown.
     *
     * @param token
     * @param session
     * @throws ConflictException
     */
    public void register(Token token, UserSession session)
            throws ConflictException, CompatibilityException {
        if (!(token instanceof SecureToken)) {
            this.logger.warning("Unrecognizable token.");
            throw new CompatibilityException("Incompatible token.");
        }
        synchronized (this.sessions) {
            if (this.sessions.containsKey(token)) {
                this.logger.warning(() -> String.format(
                    "Token %s already exists.", token));
                throw new ConflictException("Token already exists.");
            }
            this.sessions.put(token.toString(), session);
            this.logger.info(() -> String.format(
                "Token %s successfully stored.", token));
        }
    }

    /**
     * Unregister a given session given its token. If no session for the given
     * token exists, a {@code {@link NotFoundException}} will be thrown.
     *
     * @param token
     * @throws NotFoundException
     */
    public void unregister(Token token)
            throws NotFoundException, CompatibilityException {
        synchronized (this.sessions) {
            if (!this.sessions.containsKey(token.toString())) {
                this.logger.warning(() -> String.format(
                        "Token %s did not exist.", token));
                throw new NotFoundException("No session for the given token.");
            }
            this.sessions.remove(token.toString());
            this.logger.info(() -> String.format(
                "Token %s successfully forgotten.", token));
        }
    }

    /**
     * Lookup a given session given its token. If session under the given token
     * is already register, and not yet unregistered, this session is returned.
     * Otherwise a {@code {@link NotFoundException}} is thrown.
     *
     * @param token
     * @return
     * @throws NotFoundException
     */
    public UserSession lookup(Token token) throws NotFoundException {
        UserSession session = this.sessions.get(token.toString());
        if (session == null) {
            this.logger.info(() -> String.format(
                "Token not found %s.", token));
            throw new NotFoundException("No session for the given token.");
        }
        return session;
    }
}
