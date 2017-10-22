package solutions.digamma.damas.jcr.common

import solutions.digamma.damas.common.AuthenticationException
import solutions.digamma.damas.common.AuthorizationException
import solutions.digamma.damas.common.ConflictException
import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.common.WorkspaceException

import javax.jcr.ItemExistsException
import javax.jcr.ItemNotFoundException
import javax.jcr.LoginException
import javax.jcr.PathNotFoundException
import javax.jcr.RepositoryException
import javax.jcr.nodetype.ConstraintViolationException
import javax.jcr.security.AccessControlException
import java.util.logging.Level

/**
 * Repository exception conversion utility.
 *
 * This utility class converts [RepositoryException] objects to
 * corresponding [WorkspaceException] objects.
 *
 * @author Ahmad Shahwan
 */
object Exceptions {

    fun convert(e: RepositoryException): WorkspaceException {
        if (e is AccessControlException) {
            return AuthorizationException(e)
        }
        if (e is ItemNotFoundException) {
            return NotFoundException(e)
        }
        if (e is ItemExistsException) {
            return ConflictException(e)
        }
        if (e is PathNotFoundException) {
            return NotFoundException(e)
        }
        if (e is LoginException) {
            return AuthenticationException(e)
        }
        if (e is ConstraintViolationException) {
            return InternalStateException(e)
        }
        val we = WorkspaceException(e)
        we.logLevel = Level.SEVERE
        return we
    }

    fun convert(e: MutedException): WorkspaceException {
        val cause = e.cause
        if (cause is WorkspaceException) {
            return cause
        }
        if (cause is RepositoryException) {
            return convert(cause)
        }
        val we = WorkspaceException(cause)
        we.logLevel = Level.SEVERE
        return we
    }

    @Throws(WorkspaceException::class)
    fun wrap(op: () -> Any) {
        try {
            op()
        } catch (e: RepositoryException) {
            throw convert(e)
        } catch (e: MutedException) {

        }

    }

    @Throws(WorkspaceException::class)
    fun <T> wrap(op: () -> T): T {
        try {
            return op()
        } catch (e: RepositoryException) {
            throw convert(e)
        }

    }

    fun <T> mute(op: () -> T): T {
        try {
            return op()
        } catch (e: RepositoryException) {
            throw MutedException(e)
        } catch (e: WorkspaceException) {
            throw MutedException(e)
        }

    }
}
