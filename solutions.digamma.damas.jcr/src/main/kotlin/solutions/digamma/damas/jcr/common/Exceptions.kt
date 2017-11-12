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
internal object Exceptions {

    /**
     * Convert a given [RepositoryException] to its [WorkspaceException]
     * equivalent.
     *
     * @param e a repository exception
     * @return workspace exception, equivalent to the passed exception
     */
    public fun convert(e: RepositoryException) = when (e) {
        is AccessControlException -> AuthorizationException(e)
        is ItemNotFoundException -> NotFoundException(e)
        is ItemExistsException -> ConflictException(e)
        is PathNotFoundException -> NotFoundException(e)
        is LoginException -> AuthenticationException(e)
        is ConstraintViolationException -> InternalStateException(e)
        else -> WorkspaceException(e).also { it.logLevel = Level.SEVERE }
    }

    /**
     * Wraps a callable and convert any thrown exception.
     *
     * If the callable throws a [RepositoryException] exception it is converted
     * into their [WorkspaceException] counterparts.
     *
     * @param op a callable that potentially throws a [RepositoryException]
     * @return returned object by the callable, if any
     */
    @Throws(WorkspaceException::class)
    fun <T> wrap(op: () -> T) = try {
        op()
    } catch (e: RepositoryException) { throw convert(e) }

    /**
     * Wraps a callable and convert thrown exception, using a resource.
     *
     * If the callable throws a [RepositoryException] exception it is converted
     * into their [WorkspaceException] counterparts.
     *
     * An auto-closable resource is used, and assured to be properly closed.
     *
     * @param res a closable resource
     * @param op a callable that potentially throws a [RepositoryException]
     * @return returned object by the callable, if any
     */
    @Throws(WorkspaceException::class)
    fun <C: AutoCloseable, T> wrap(res: C, op: (res: C) -> T) = try {
        res.use { op(it) }
    } catch (e: RepositoryException) { throw convert(e) }
}
