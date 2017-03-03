package solutions.digamma.damas.jcr.auth;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.ResourceBusyException;

import javax.jcr.Session;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * JCR session box.
 *
 * @author Ahmad Shahwan
 */
public class UserSession implements AutoCloseable {

    private final static long TIMEOUT = 60;

    private Session session;

    private boolean open = false;

    private ReentrantLock lock = new ReentrantLock();

    /**
     * Constructor.
     *
     * @param session JCR session.
     */
    UserSession(Session session) {
        this.session = session;
    }

    public synchronized UserSession open() {
        this.open = true;
        return this;
    }

    /**
     * Use the underling JCR session, acquiring the lock to this session.
     * If session is already in use, a maximum of {@code TIMEOUT} seconds is
     * waited, after which a {@code ResourceBusyException} is thrown.
     *
     * This method can only be called while session is open. Calling it when
     * session is closed throws an {@code IllegalStateException}.
     *
     * @return The underling JCR session.
     * @throws ResourceBusyException When timeout is reached while the session
     * is still locked.
     * @throws DocumentException When thread is interrupted.
     */
    public synchronized Session use() throws DocumentException {
        if (!this.open) {
            throw new IllegalStateException("Session is not open.");
        }
        try {
            if (this.lock.tryLock(TIMEOUT, TimeUnit.SECONDS)) {
                return this.session;
            }
            else {
                throw new ResourceBusyException(
                        "Session in use by another thread.");
            }
        } catch (InterruptedException e) {
            throw new DocumentException("Thread interrupted.", e);
        }
    }

    @Override
    public synchronized void close() {
        this.open = false;
        while (this.lock.isLocked()) this.lock.unlock();
    }

}
