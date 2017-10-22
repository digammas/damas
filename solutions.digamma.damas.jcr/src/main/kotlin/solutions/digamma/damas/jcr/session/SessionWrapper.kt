package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.ResourceBusyException

import javax.jcr.Session
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * JCR session box.
 *
 * @author Ahmad Shahwan
 */
class SessionWrapper
/**
 * Constructor.
 *
 * @param session JCR session.
 */
(private val session: Session) : AutoCloseable {

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
    fun open(): SessionWrapper {
        try {
            return if (this.lock.tryLock(TIMEOUT, TimeUnit.SECONDS)) {
                this
            } else {
                throw ResourceBusyException(
                        "Session in use by another thread.")
            }
        } catch (e: InterruptedException) {
            throw WorkspaceException("Thread interrupted.", e)
        }

    }

    @Throws(WorkspaceException::class)
    private fun checkUsability() {
        if (!this.lock.isLocked) {
            throw InternalStateException("Session not open yet.")
        }
        if (!this.lock.tryLock()) {
            throw ResourceBusyException(
                    "Session open and in use by another thread.")
        }
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

    @Synchronized override fun close() {
        while (this.lock.isLocked) this.lock.unlock()
    }

    companion object {

        private val TIMEOUT: Long = 60
    }

}
