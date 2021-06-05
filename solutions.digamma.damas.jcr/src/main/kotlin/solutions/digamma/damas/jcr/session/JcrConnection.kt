package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.session.Connection
import java.util.EmptyStackException
import java.util.Stack

internal class JcrConnection : Connection {

    val session: TransactionalSession
    var closed = false

    @Throws(WorkspaceException::class)
    constructor(session: TransactionalSession) {
        this.session = session
        this.session.acquire()
        JcrConnection.SESSION.get().push(this.session)
    }

    @Throws(WorkspaceException::class)
    override fun commit() {
        this.session.commit()
    }

    @Throws(WorkspaceException::class)
    override fun rollback() {
        this.session.rollback()
    }

    @Throws(WorkspaceException::class)
    override fun close() {
        synchronized(this.closed) {
            if (this.closed) {
                throw AlreadyClosedException()
            }
            this.closed = true
            JcrConnection.SESSION.get().pop()
            this.session.commit()
            this.session.release()
        }
    }

    companion object {
        private val SESSION = ThreadLocal.withInitial {
            Stack<TransactionalSession>()
        }

        @Throws(NotFoundException::class)
        fun get(): TransactionalSession = try {
            SESSION.get().peek()
        } catch (_: EmptyStackException) {
            throw NoAuthenticationFountException()
        }
    }
}

internal class NoAuthenticationFountException :
        NotFoundException("No authentication found in context.")

internal class AlreadyClosedException() :
        IllegalStateException("Session already closed.")
