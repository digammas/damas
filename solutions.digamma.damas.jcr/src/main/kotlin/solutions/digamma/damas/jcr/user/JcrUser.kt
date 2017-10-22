package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.MisuseException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.inspection.NotNull
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.ItemNamespace
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.User

import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import java.util.regex.Pattern

/**
 * JCR-node-backed user implementation.
 *
 * @author Ahmad Shahwan
 */
class JcrUser
/**
 * Constructor with JCR-node.
 *
 * @param node JCR node
 * @throws WorkspaceException
 */
@Throws(WorkspaceException::class)
protected constructor(node: Node) : JcrSubject(node), User {

    @Throws(WorkspaceException::class)
    override fun getLogin(): String {
        return Exceptions.wrap<String>({ this.node.name })
    }

    @Throws(WorkspaceException::class)
    override fun setPassword(value: String) {

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
            throw MisuseException("Invalid email address.")
        }
    }

    companion object {

        val EMAIL_ADDRESS_REGEX = Pattern.compile(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

        @Throws(WorkspaceException::class)
        fun of(node: Node): JcrUser {
            return JcrUser(node)
        }

        @Throws(WorkspaceException::class)
        fun from(session: Session, login: String): JcrUser {
            try {
                val root = session.getNode(JcrSubject.ROOT_PATH)
                return of(root.addNode(login, TypeNamespace.USER))
            } catch (e: RepositoryException) {
                throw Exceptions.convert(e)
            }

        }
    }
}
