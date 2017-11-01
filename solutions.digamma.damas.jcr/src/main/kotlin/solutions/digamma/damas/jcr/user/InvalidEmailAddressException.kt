package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.InvalidArgumentException

internal class InvalidEmailAddressException():
        InvalidArgumentException("Invalid email address.")