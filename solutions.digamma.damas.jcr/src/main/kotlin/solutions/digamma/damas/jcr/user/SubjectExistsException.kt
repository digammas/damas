package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.ConflictException

internal class SubjectExistsException(name: String, cause: Exception?):
        ConflictException("Subject $name already exists.", cause)