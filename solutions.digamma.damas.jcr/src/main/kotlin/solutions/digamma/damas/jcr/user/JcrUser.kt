package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.InvalidArgumentException
import solutions.digamma.damas.common.MisuseException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.inspection.NotNull
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.ItemNamespace
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.User
import java.util.regex.Pattern
import javax.jcr.ItemExistsException
import javax.jcr.Node
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
class JcrUser
@Throws(WorkspaceException::class)
private constructor(node: Node) : JcrSubject(node), User {

    @Throws(WorkspaceException::class)
    override fun getLogin(): String = Exceptions.wrap { this.node.name }

    @Throws(WorkspaceException::class)
    override fun setPassword(value: String) {
        /* TODO set password logic */
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
        return getString(ItemNamespace.FIRST_NAME)
    }

    @Throws(WorkspaceException::class)
    override fun setFirstName(value: String) {
        setString(ItemNamespace.FIRST_NAME, value)
    }

    @Throws(WorkspaceException::class)
    override fun getLastName(): String {
        return getString(ItemNamespace.LAST_NAME)
    }

    @Throws(WorkspaceException::class)
    override fun setLastName(value: String) {
        setString(ItemNamespace.LAST_NAME, value)
    }

    @Throws(WorkspaceException::class)
    override fun getMemberships(): List<String> {
        return this.getStrings(ItemNamespace.GROUPS)
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

    companion object {

        val EMAIL_ADDRESS_REGEX = Pattern.compile(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

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
