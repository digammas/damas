package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.InvalidArgumentException

internal class InsecurePasswordException internal constructor():
        InvalidArgumentException("Password doesn't meet requirements.")