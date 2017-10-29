package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.ConflictException

class SubjectExistsException(name: String, cause: Exception?):
        ConflictException("Subject $name already exists.", cause) {
}