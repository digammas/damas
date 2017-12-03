package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.MisuseException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.ItemNamespace
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.User
import java.security.MessageDigest
import java.util.Arrays
import java.util.Base64
import java.util.Collections
import java.util.Random
import java.util.regex.Pattern
import javax.jcr.ItemExistsException
import javax.jcr.Node
import javax.jcr.Session

/**
 * JCR-node-backed user implementation.
 *
 * Users are identified by their login. User login cannot be modified.
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
    override fun getMemberships(): List<String> = try {
        this.getStrings(ItemNamespace.GROUPS).map {
            this.node.session.getNodeByIdentifier(it).name
        }
    } catch (_: NotFoundException) {
        Collections.emptyList()
    }

    @Throws(WorkspaceException::class)
    fun setMemberships(groups: List<String>) {
        this.setStrings(ItemNamespace.GROUPS, groups.map {
            this.node.session.getNode("$ROOT_PATH/$it").identifier
        })
    }

    @Throws(InternalStateException::class)
    override fun checkCompatibility() =
            checkTypeCompatibility(TypeNamespace.USER)

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
        val password = salt + digest
        this.setString(ItemNamespace.PASSWORD,
                Base64.getEncoder().encodeToString(password))
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
        val password = Base64.getDecoder().decode(
                this.getString(ItemNamespace.PASSWORD))
        val salt = password.sliceArray(0..31)
        val digest = MD.digest(salt + value.toByteArray())
        val stored = password.sliceArray(32..63)
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
        other.memberships?.let { this.memberships = it }
        other.memberships?.let { this.memberships = it }
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
