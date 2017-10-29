package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.common.CompatibilityException
import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.common.ConflictException
import solutions.digamma.damas.common.NotFoundException

import javax.inject.Inject
import javax.inject.Singleton
import java.util.HashMap
import java.util.logging.Logger

/**
 * @author Ahmad Shahwan
 */
@Singleton
internal class SessionBookkeeper {

    @Inject
    private lateinit var logger: Logger

    private val sessions: MutableMap<String, SessionWrapper>

    init {
        this.sessions = HashMap()
    }

    /**
     * Register a given session under the given token. If a session was
     * registered with the same token, a `{ ConflictException}` will
     * be thrown.
     *
     * @param token
     * @param session
     * @throws ConflictException
     */
    @Throws(ConflictException::class, CompatibilityException::class)
    fun register(token: Token, session: SessionWrapper) {
        if (token !is SecureToken) {
            this.logger.warning("Unrecognizable token.")
            throw CompatibilityException("Incompatible token.")
        }
        synchronized(this.sessions) {
            if (this.sessions.containsKey(token.secret)) {
                this.logger.warning("Token $token already exists.")
                throw ConflictException("Token already exists.")
            }
            this.sessions.put(token.secret, session)
            this.logger.info { "Token %s successfully stored.".format(token) }
        }
    }

    /**
     * Unregister a given session given its token. If no session for the given
     * token exists, a `{ NotFoundException}` will be thrown.
     *
     * @param token
     * @throws NotFoundException
     */
    @Throws(NotFoundException::class)
    fun unregister(token: Token) {
        synchronized(this.sessions) {
            if (!this.sessions.containsKey(token.secret)) {
                this.logger.warning {
                    "Token %s did not exist.".format(token)
                }
                throw NotFoundException("No session for the given token.")
            }
            this.sessions.remove(token.secret)
            this.logger.info {
                "Token %s successfully forgotten.".format(token)
            }
        }
    }

    /**
     * Lookup a given session given its token. If session under the given token
     * is already register, and not yet unregistered, this session is returned.
     * Otherwise a `{ NotFoundException}` is thrown.
     *
     * @param token
     * @return
     * @throws NotFoundException
     */
    @Throws(NotFoundException::class)
    fun lookup(token: Token?): SessionWrapper {
        if (token == null) {
            throw NotFoundException("Token is null.")
        }
        val session = this.sessions[token.secret]
        if (session == null) {
            this.logger.info { "Token not found %s.".format(token) }
            throw NotFoundException("No session for the given token.")
        }
        return session
    }
}
