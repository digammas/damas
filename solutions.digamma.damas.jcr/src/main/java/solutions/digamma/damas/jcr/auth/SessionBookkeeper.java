package solutions.digamma.damas.jcr.auth;

import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.ConflictException;
import solutions.digamma.damas.NotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ahmad Shahwan
 */
public class SessionBookkeeper {

    private final Map<SecureToken, UserSession> sessions;

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
    void register(Token token, UserSession session)
            throws ConflictException, CompatibilityException {
        if (!(token instanceof SecureToken)) {
            throw new CompatibilityException("Incompatible token.");
        }
        synchronized (this.sessions) {
            if (this.sessions.containsKey(token)) {
                throw new ConflictException("Token already exists.");
            }
            this.sessions.put((SecureToken) token, session);
        }
    }

    /**
     * Unregister a given session given its token. If no session for the given
     * token exists, a {@code {@link NotFoundException}} will be thrown.
     *
     * @param token
     * @throws NotFoundException
     */
    void unregister(Token token)
            throws NotFoundException, CompatibilityException {
        if (!(token instanceof SecureToken)) {
            throw new CompatibilityException("Unrecognized token.");
        }
        synchronized (this.sessions) {
            if (!this.sessions.containsKey(token)) {
                throw new NotFoundException("No session for the given token.");
            }
            this.sessions.remove(token);
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
        UserSession session = this.sessions.get(token);
        if (session == null) {
            throw new NotFoundException("No session for the given token.");
        }
        return session;
    }
}
