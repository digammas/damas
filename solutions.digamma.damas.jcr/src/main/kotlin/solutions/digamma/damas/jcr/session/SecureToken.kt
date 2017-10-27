package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.inspection.NotNull

import java.math.BigInteger
import java.security.SecureRandom

/**
 * Statistically secure token.
 *
 * @author Ahmad Shahwan
 */
class SecureToken : Token {

    private val token: String

    /**
     * Constructor.
     */
    init {
        this.token = SecureToken.nextToken()
    }

    override fun getSecret() = this.token

    /**
     * Two token with no secret do not equal each other.
     *
     * @param other object to compare with
     * @return equality
     */
    override fun equals(other: Any?): Boolean =
        other is Token && this.secret == other.secret

    companion object {

        private val RANDOM = SecureRandom()

        private fun nextToken(): String {
            return BigInteger(132, RANDOM).toString(64)
        }
    }
}
