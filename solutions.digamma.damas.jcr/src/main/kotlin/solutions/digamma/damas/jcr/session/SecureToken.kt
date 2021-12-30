package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.session.UserToken

import java.math.BigInteger
import java.security.SecureRandom
import java.util.Date

/**
 * Statistically secure token.
 *
 * @author Ahmad Shahwan
 */
internal class SecureToken(
    private val login: String,
) : UserToken {

    private val token: String
    private val creation: Date

    /**
     * Constructor.
     */
    init {
        this.token = SecureToken.nextToken()
        this.creation = Date()
    }

    override fun getSecret() = this.token

    /**
     * Two token with no secret do not equal each other.
     *
     * @param other object to compare with
     * @return equality
     */
    override fun equals(other: Any?): Boolean =
        other is UserToken &&
                this.secret == other.secret &&
                this.userLogin == other.userLogin

    override fun hashCode(): Int {
        return this.secret.hashCode()
    }

    companion object {

        private val RANDOM = SecureRandom()

        private fun nextToken(): String {
            return BigInteger(132, RANDOM).toString(64)
        }
    }

    override fun getUserLogin() = this.login

    override fun getCreationDate() = this.creation

    override fun getExpirationDate() = Date(Long.MAX_VALUE)
}
