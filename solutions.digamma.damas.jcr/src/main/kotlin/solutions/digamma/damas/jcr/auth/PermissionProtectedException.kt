package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.common.ConflictException

/**
 * Exception thrown upon attempt to modify protected permission.
 */
internal class PermissionProtectedException
internal constructor(subject: String, path: String):
        ConflictException("Permission for $subject at $path is protected.")