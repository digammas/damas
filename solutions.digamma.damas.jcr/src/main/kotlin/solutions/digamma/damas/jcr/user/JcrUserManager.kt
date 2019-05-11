package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.config.Configuration
import solutions.digamma.damas.config.Fallback
import solutions.digamma.damas.entity.Page
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
import javax.jcr.Property
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
            JcrUser.of(session.getNode("${JcrSubject.ROOT_PATH}/$id"))

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun delete(session: Session, id: String) =
            retrieve(session, id).remove()

    @Throws(WorkspaceException::class, RepositoryException::class)
    override fun find(session: Session, offset: Int, size: Int, query: Any?):
            Page<User> {
        val sql2 = """
           SELECT * FROM [${TypeNamespace.USER}] AS user
           WHERE ISDESCENDANTNODE(user, '${JcrSubject.ROOT_PATH}')
           ORDER BY user.[${Property.JCR_CREATED}]
        """
        return this.query(session, sql2, offset, size, JcrUser.Companion::of)
    }

    @Throws(WorkspaceException::class)
    override fun updatePassword(id: String, value: String) {
        pwRegex.matcher(value).matches() || throw InsecurePasswordException()
        Exceptions.check {
            val user = this.retrieve(this.getSession(), id)
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