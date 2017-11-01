package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.auth.Token
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.config.Configuration
import solutions.digamma.damas.config.Fallback
import solutions.digamma.damas.entity.Page
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.common.ResultPage
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import solutions.digamma.damas.user.User
import solutions.digamma.damas.user.UserManager
import java.util.Collections
import java.util.regex.Pattern
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.RepositoryException
import javax.jcr.Session

@Singleton
internal class JcrUserManager : JcrCrudManager<User>(),
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
    override fun create(session: Session, pattern: User) =
            JcrUser.from(session, pattern.login).also { it.update(pattern) }

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun update(session: Session, id: String, pattern: User) =
        retrieve(session, id).also { it.update(pattern) }

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun retrieve(session: Session, id: String) =
            JcrUser.of(session.getNodeByIdentifier(id))

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun delete(session: Session, id: String) =
            retrieve(session, id).remove()

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun find(session: Session, offset: Int, size: Int, query: Any?):
            Page<User> = ResultPage(Collections.emptyList())

    @Throws(WorkspaceException::class)
    override fun updatePassword(token: Token, id: String, value: String) {
        pwRegex.matcher(value).matches() || throw InsecurePasswordException()
        val user = this.retrieve(token, id) as JcrUser
        Exceptions.wrap {
            user.setPassword(value)
        }
    }

    companion object {

        /**
         * At least 8 character, at least one uppercase, one lowercase, and one
         * number.
         */
        const val PASSWORD_DEFAULT_PATTERN =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d$@$!%*?&]{8,}$"
    }
}