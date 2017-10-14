package solutions.digamma.damas.jcr.session;

import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.InternalStateException;
import solutions.digamma.damas.ResourceBusyException;

import javax.jcr.Session;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * JCR session box.
 *
 * @author Ahmad Shahwan
 */
public class SessionWrapper implements AutoCloseable {

    private final static long TIMEOUT = 60;

    private final Session session;

    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Constructor.
     *
     * @param session JCR session.
     */
    public SessionWrapper(Session session) {
        this.session = session;
    }

    /**
     * Open the session for toJcrSession, acquiring an exclusive lock.
     * If session is already in toJcrSession, a maximum of {@code TIMEOUT}
     * seconds is waited, after which a {@code ResourceBusyException} is thrown.
     *
     * @return The underling JCR session.
     * @throws ResourceBusyException When timeout is reached while the session
     * is still locked.
     * @throws WorkspaceException When thread is interrupted.
     */
    public SessionWrapper open() throws WorkspaceException {
        try {
            if (this.lock.tryLock(TIMEOUT, TimeUnit.SECONDS)) {
                return this;
            }
            else {
                throw new ResourceBusyException(
                        "Session in use by another thread.");
            }
        } catch (InterruptedException e) {
            throw new WorkspaceException("Thread interrupted.", e);
        }
    }

    private void checkUsability() throws WorkspaceException {
        if (!this.lock.isLocked()) {
            throw new InternalStateException("Session not open yet.");
        }
        if (!this.lock.tryLock()) {
            throw new ResourceBusyException(
                    "Session open and in use by another thread.");
        }
    }

    /**
     * Use the underling JCR session.
     *
     * This method can only be called while session is open. Calling it when
     * session is closed throws an {@code InternalStateException}.
     *
     * @return The underling JCR session.
     * @throws InternalStateException When session is not yet open.
     * @throws ResourceBusyException When session is open and used by another
     * thread.
     */
    public Session getSession() throws WorkspaceException {
        this.checkUsability();
        return this.session;
    }

    @Override
    public synchronized void close() {
        while (this.lock.isLocked()) this.lock.unlock();
    }

}
