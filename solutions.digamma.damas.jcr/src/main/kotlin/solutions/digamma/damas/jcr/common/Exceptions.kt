package solutions.digamma.damas.jcr.common

import solutions.digamma.damas.common.AuthenticationException
import solutions.digamma.damas.common.AuthorizationException
import solutions.digamma.damas.common.ConflictException
import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.common.WorkspaceException.Origin.INTERNAL
import javax.jcr.ItemExistsException
import javax.jcr.ItemNotFoundException
import javax.jcr.LoginException
import javax.jcr.PathNotFoundException
import javax.jcr.RepositoryException
import javax.jcr.nodetype.ConstraintViolationException
import javax.jcr.security.AccessControlException

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
    internal fun convert(e: RepositoryException) = when (e) {
        is AccessControlException -> AuthorizationException(e)
        is ItemNotFoundException -> NotFoundException(e)
        is ItemExistsException -> ConflictException(e)
        is PathNotFoundException -> NotFoundException(e)
        is LoginException -> AuthenticationException(e)
        is ConstraintViolationException -> InternalStateException(e)
        else -> WorkspaceException(e).also { it.origin = INTERNAL }
    }

    /**
     * Wraps a callable and convert any thrown exception to a checked exception.
     *
     * If the callable throws a [RepositoryException] exception it is converted
     * into its [WorkspaceException] counterparts.
     *
     * @param op a callable that potentially throws a [RepositoryException]
     * @return returned object by the callable, if any
     */
    @Throws(WorkspaceException::class)
    internal fun <T> check(op: () -> T) = try {
        op()
    } catch (e: RepositoryException) {
        throw convert(e)
    } catch (e: RuntimeException) {
        val cause = e.cause
        throw when (cause) {
            is RepositoryException -> convert(cause)
            is WorkspaceException -> cause
            else -> e
        }
    }

    /**
     * Wraps a callable and convert any thrown exception to an unchecked
     * exception.
     *
     * If the callable throws a [RepositoryException] exception it is converted
     * into an [IllegalStateException] exception whose cause is the
     * [WorkspaceException] counterparts of the original exception.
     *
     * If the callable throws a checked exception it is converted into an
     * [IllegalStateException] exception whose cause is the original exception.

     * @param op a callable that potentially throws an exception
     * @return returned object by the callable, if any
     */
    internal fun <T> uncheck(op: () -> T) = try {
        op()
    } catch (e: RuntimeException) {
        /* Prevent fallback */
        throw e;
    } catch (e: RepositoryException) {
        throw IllegalStateException(convert(e))
    } catch (e: Exception) {
        throw IllegalStateException(e);
    }
}
