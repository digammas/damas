package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.InvalidArgumentException

internal class InsecurePasswordException():
        InvalidArgumentException("Password doesn't meet requirements.")