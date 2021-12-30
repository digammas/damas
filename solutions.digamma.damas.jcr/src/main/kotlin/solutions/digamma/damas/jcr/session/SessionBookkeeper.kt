package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.common.CompatibilityException
import solutions.digamma.damas.session.Token
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

    private val sessions: MutableMap<String, TransactionalSession>

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
    fun register(token: SecureToken, session: TransactionalSession) {
        synchronized(this.sessions) {
            if (this.sessions.containsKey(token.secret)) {
                this.logger.warning { "Token ${token.secret} already exists." }
                throw TokenAlreadyExistsException()
            }
            this.sessions[token.secret] = session
            this.logger.info { "Token ${token.secret} stored." }
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
                this.logger.warning { "Token ${token.secret} did not exist." }
                throw NoSessionForTokenException()
            }
            this.sessions.remove(token.secret)
            this.logger.info { "Token ${token.secret} forgotten." }
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
    fun lookup(token: Token): TransactionalSession {
        val session = this.sessions[token.secret]
        if (session == null) {
            this.logger.info { "Token not found ${token.secret}." }
            throw NoSessionForTokenException()
        }
        return session
    }
}

class TokenAlreadyExistsException :
        ConflictException("Token already exists.")
class NoSessionForTokenException :
        NotFoundException("No session for the given token.")
