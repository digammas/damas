package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.InvalidArgumentException
import solutions.digamma.damas.common.MisuseException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.ItemNamespace
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.User
import java.security.MessageDigest
import java.util.Arrays
import java.util.Base64
import java.util.Random
import java.util.regex.Pattern
import javax.jcr.ItemExistsException
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * JCR-node-backed user implementation.
 *
 * @constructor Constructor with JCR-node.
 *
 * @param node JCR node
 * @throws WorkspaceException
 *
 * @author Ahmad Shahwan
 */
internal class JcrUser
@Throws(WorkspaceException::class)
private constructor(node: Node) : JcrSubject(node), User {

    @Throws(WorkspaceException::class)
    override fun getId(): String = Exceptions.wrap { this.login }

    @Throws(WorkspaceException::class)
    override fun getLogin(): String = Exceptions.wrap { this.node.name }

    @Throws(WorkspaceException::class)
    override fun getEmailAddress() =
        this.getString(ItemNamespace.EMAIL)

    @Throws(WorkspaceException::class)
    override fun setEmailAddress(value: String) {
        this.validateEmailAddress(value)
        this.setString(ItemNamespace.EMAIL, value)
    }

    @Throws(WorkspaceException::class)
    override fun getFirstName() =
        this.getString(ItemNamespace.NAME)

    @Throws(WorkspaceException::class)
    override fun setFirstName(value: String) {
        this.setString(ItemNamespace.NAME, value)
    }

    @Throws(WorkspaceException::class)
    override fun getLastName() =
        this.getString(ItemNamespace.SURNAME)

    @Throws(WorkspaceException::class)
    override fun setLastName(value: String) {
        this.setString(ItemNamespace.SURNAME, value)
    }

    @Throws(WorkspaceException::class)
    override fun getMemberships() =
        this.getStrings(ItemNamespace.GROUPS)

    /**
     * Set user's password.
     * This setter doesn't perform any password validity check.
     *
     * @param value password
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun setPassword(value: String) {
        val salt = ByteArray(32)
        RANDOM.nextBytes(salt)
        val digest = MD.digest(salt + value.toByteArray())
        this.setString(ItemNamespace.PASSWORD,
                Base64.getEncoder().encodeToString(digest))
        this.setString(ItemNamespace.SALT,
                Base64.getEncoder().encodeToString(salt))
    }

    /**
     * Check if the given password matches the salted and hashed password of
     * this user.
     *
     * @param value             the password to be checked
     * @return                  whether provided password matches user's
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun checkPassword(value: String): Boolean {
        val salt = Base64.getDecoder().decode(
                this.getString(ItemNamespace.SALT))
        val digest = MD.digest(salt + value.toByteArray())
        val stored = Base64.getDecoder().decode(
                this.getString(ItemNamespace.PASSWORD))
        return Arrays.equals(stored, digest)
    }

    @Throws(MisuseException::class)
    private fun validateEmailAddress(value: String) {
        EMAIL_ADDRESS_REGEX.matcher(value).matches() ||
                throw InvalidEmailAddressException()
    }

    /**
     * Update user with user information.
     *
     * @param other                 pattern to use for update
     * @throws WorkspaceException
     */
    @Throws(WorkspaceException::class)
    fun update(other: User) {
        other.firstName?.let { this.firstName = it }
        other.lastName?.let { this.lastName = it }
        other.emailAddress?.let { this.emailAddress = it }
    }

    companion object {

        /**
         * Entropy generator used to generate password salts.
         */
        private val RANDOM = Random()

        private val EMAIL_ADDRESS_REGEX = Pattern.compile(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")!!

        /**
         * Algorith used to digest password.
         */
        private val MD = MessageDigest.getInstance("SHA-256")!!

        @Throws(WorkspaceException::class)
        fun of(node: Node) = JcrUser(node)

        @Throws(WorkspaceException::class)
        fun from(session: Session, login: String): JcrUser = Exceptions.wrap {
            val root = session.getNode(JcrSubject.ROOT_PATH)
            try {
                of(root.addNode(login, TypeNamespace.USER))
            } catch(e: ItemExistsException) {
                throw SubjectExistsException(login, e)
            }
        }
    }
}
