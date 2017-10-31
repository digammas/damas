package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.InvalidArgumentException
import solutions.digamma.damas.common.MisuseException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.ItemNamespace
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.User
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.SecureRandom
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
    override fun getLogin(): String = Exceptions.wrap { this.node.name }

    @Throws(WorkspaceException::class)
    override fun setPassword(value: String) {
        val salt = ByteArray(32)
        SECURE_RANDOM.nextBytes(salt)
        val digest = MD.digest(salt + value.toByteArray())
        this.node.setProperty(ItemNamespace.PASSWORD, toBinary(digest))
        this.node.setProperty(ItemNamespace.SALT, toBinary(salt))
    }

    @Throws(WorkspaceException::class)
    override fun getEmailAddress(): String {
        return getString(ItemNamespace.EMAIL)
    }

    @Throws(WorkspaceException::class)
    override fun setEmailAddress(value: String) {
        validateEmailAddress(value)
        setString(ItemNamespace.EMAIL, value)
    }

    @Throws(WorkspaceException::class)
    override fun getFirstName(): String {
        return getString(ItemNamespace.NAME)
    }

    @Throws(WorkspaceException::class)
    override fun setFirstName(value: String) {
        setString(ItemNamespace.NAME, value)
    }

    @Throws(WorkspaceException::class)
    override fun getLastName(): String {
        return getString(ItemNamespace.SURNAME)
    }

    @Throws(WorkspaceException::class)
    override fun setLastName(value: String) {
        setString(ItemNamespace.SURNAME, value)
    }

    @Throws(WorkspaceException::class)
    override fun getMemberships(): List<String> {
        return this.getStrings(ItemNamespace.GROUPS)
    }

    /**
     * Check if the given password matches the salted and hashed password of
     * this user.
     *
     * @param value             the password to be checked
     * @return                  whether provided password matches user's
     * @throws RepositoryException
     */
    @Throws(RepositoryException::class)
    fun checkPassword(value: String): Boolean {
        val salt = ByteArray(32)
        this.node.getProperty(ItemNamespace.SALT).binary.read(salt, 0)
        val digest = MD.digest(salt + value.toByteArray())
        val stored = ByteArray(digest.size)
        this.node.getProperty(ItemNamespace.SALT).binary.read(stored, 0)
        return stored.equals(digest)
    }

    @Throws(MisuseException::class)
    private fun validateEmailAddress(value: String?) {
        if (value == null || !EMAIL_ADDRESS_REGEX.matcher(value).find()) {
            throw InvalidArgumentException("Invalid email address.")
        }
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

    @Throws(RepositoryException::class)
    private fun toBinary(bytes: ByteArray) = this.node.session.valueFactory
            .createBinary(ByteArrayInputStream(bytes))

    companion object {

        val EMAIL_ADDRESS_REGEX = Pattern.compile(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

        val SECURE_RANDOM = SecureRandom.getInstanceStrong()

        val MD = MessageDigest.getInstance("SHA-256")

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
