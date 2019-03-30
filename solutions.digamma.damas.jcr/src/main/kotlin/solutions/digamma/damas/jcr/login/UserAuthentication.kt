package solutions.digamma.damas.jcr.login

import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.session.TransactionalSession
import solutions.digamma.damas.login.Authentication
import java.util.EmptyStackException
import java.util.Stack

internal class UserAuthentication : Authentication {

    val session: TransactionalSession
    var closed = false

    @Throws(WorkspaceException::class)
    constructor(session: TransactionalSession) {
        this.session = session
        this.session.acquire()
        SESSION.get().push(this.session)
    }

    override fun close() {
        synchronized(this.closed) {
            if (this.closed) {
                throw AlreadyClosedException()
            }
            this.closed = true
            SESSION.get().pop()
            session.commit()
            session.release()
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