package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.InvalidArgumentException

internal class InvalidEmailAddressException internal constructor():
        InvalidArgumentException("Invalid email address.")