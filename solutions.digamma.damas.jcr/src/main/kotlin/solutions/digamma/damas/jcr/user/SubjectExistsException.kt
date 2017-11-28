package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.ConflictException

internal class SubjectExistsException
internal constructor(name: String, cause: Exception?):
        ConflictException("Subject $name already exists.", cause)