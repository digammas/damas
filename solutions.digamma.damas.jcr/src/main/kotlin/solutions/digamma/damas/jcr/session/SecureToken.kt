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


    override fun getSecret(): String {
        return this.token
    }

    override fun equals(other: Any?): Boolean {
        /* Two token with no secret do not equal each other. */
        return other is Token &&
                this.secret != null &&
                this.secret == other.secret
    }

    companion object {

        private val RANDOM = SecureRandom()

        private fun nextToken(): String {
            return BigInteger(132, RANDOM).toString(64)
        }
    }
}
