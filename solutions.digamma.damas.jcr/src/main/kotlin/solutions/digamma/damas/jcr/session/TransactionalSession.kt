package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.ResourceBusyException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.session.UserSession
import java.util.Date

import javax.jcr.Session
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * JCR session wrapper, with transactional behavior.
 *
 * @param session JCR session.
 * @author Ahmad Shahwan
 */
internal class TransactionalSession(
    @Transient
    private val session: Session,
    private val login: String = session.userID,
) : UserSession {

    private val creation: Date = Date()
    private val lock = ReentrantLock()

    /**
     * Open the session for toJcrSession, acquiring an exclusive lock.
     * If session is already in toJcrSession, a maximum convert `TIMEOUT`
     * seconds is waited, after which a `ResourceBusyException` is thrown.
     *
     * @return The underling JCR session.
     * @throws ResourceBusyException When timeout is reached while the session
     * is still locked.
     * @throws WorkspaceException When thread is interrupted.
     */
    @Throws(WorkspaceException::class)
    fun acquire(): TransactionalSession = try {
        if (this.lock.tryLock(TIMEOUT, TimeUnit.SECONDS)) this
        else throw SessionInUseException()
    } catch (e: InterruptedException) {
        throw WorkspaceException(e)
    }

    @Synchronized fun release() {
        if (this.lock.isLocked) this.lock.unlock()
    }

    /**
     * Use the underling JCR session.
     *
     * This method can only be called while session is open. Calling it when
     * session is closed throws an `InternalStateException`.
     *
     * @return The underling JCR session.
     * @throws InternalStateException When session is not yet open.
     * @throws ResourceBusyException When session is open and used by another
     * thread.
     */
    @Throws(WorkspaceException::class)
    fun getSession(): Session {
        this.checkUsability()
        return this.session
    }

    @Throws(WorkspaceException::class)
    fun commit() {
        this.checkUsability()
        Exceptions.check { this.session.save() }
    }

    @Throws(WorkspaceException::class)
    fun rollback() {
        this.checkUsability()
        Exceptions.check { this.session.refresh(false) }
    }

    @Throws(WorkspaceException::class)
    private fun checkUsability() {
        this.lock.isLocked || throw SessionNotOpenException()
        this.lock.isHeldByCurrentThread || throw SessionOpenAndInUseException()
    }

    override fun getUserLogin(): String = this.login

    override fun getCreationDate(): Date = this.creation

    override fun getExpirationDate(): Date = Date(Long.MAX_VALUE)

    companion object {

        private const val TIMEOUT: Long = 60
    }

}

class SessionInUseException :
        ResourceBusyException("Session in use by another thread.")
class SessionNotOpenException :
        InternalStateException("Session not open yet.")
class SessionOpenAndInUseException :
        ResourceBusyException("Session open and in use by another thread.")
