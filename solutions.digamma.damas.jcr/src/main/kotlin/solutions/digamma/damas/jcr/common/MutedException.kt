package solutions.digamma.damas.jcr.common

/**
 * Muted exception.
 *
 * @author Ahmad Shahwan
 */
class MutedException internal constructor(
        message: String,
        /**
        * Cause as an [Exception].
        */
        override val cause: Exception) : RuntimeException(message, cause) {

    internal constructor(cause: Exception) : this("Exception muted.", cause)
}
