package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.config.Configuration
import solutions.digamma.damas.config.Fallback
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.user.User
import solutions.digamma.damas.user.UserManager
import java.util.regex.Pattern
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.Node
import javax.jcr.RepositoryException

@Singleton
internal open class JcrUserManager : JcrCrudManager<User>(),
        JcrSearchEngine<User>,
        UserManager {

    @Inject
    @Configuration("password.pattern")
    @Fallback(PASSWORD_DEFAULT_PATTERN)
    private lateinit var passwordPattern: String
    private lateinit var pwRegex: Pattern

    @PostConstruct
    fun init() {
        this.pwRegex = Pattern.compile(this.passwordPattern)
    }

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun doCreate(pattern: User): JcrUser {
        return JcrUser.from(this.session, pattern.login).also {
            it.update(pattern)
        }
    }

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun doUpdate(id: String, pattern: User) =
        doRetrieve(id).also { it.update(pattern) }

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun doRetrieve(id: String) =
            JcrUser.of(this.session.getNode("${JcrSubject.ROOT_PATH}/$id"))

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun doDelete(id: String) =
            doRetrieve(id).remove()

    @Throws(WorkspaceException::class)
    override fun updatePassword(id: String, value: String) {
        pwRegex.matcher(value).matches() || throw InsecurePasswordException()
        Exceptions.check {
            val user = this.doRetrieve(id)
            user.setPassword(value)
        }
    }

    override fun getNodePrimaryType() = TypeNamespace.USER

    override fun getDefaultRootPath() = JcrSubject.ROOT_PATH

    override fun fromNode(node: Node) = JcrUser.of(node)

    companion object {

        /**
         * At least 8 character, at least one uppercase, one lowercase, and one
         * number.
         */
        const val PASSWORD_DEFAULT_PATTERN =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d$@$!%*?&]{8,}$"
    }
}
