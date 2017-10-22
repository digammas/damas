package solutions.digamma.damas.jcr.repo

import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.Credentials
import javax.jcr.Repository
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.SimpleCredentials

/**
 * System-wide repository functionality.
 *
 * @author Ahmad Shahwan
 */
@Singleton
class SystemRepository @Inject
constructor(private val repository: Repository) {

    private var superuser: Session? = null
    private var readonly: Session? = null


    /**
     * Retrieve a valid superuser session.
     *
     * @return JCR superuser session
     * @throws RepositoryException
     */
    val superuserSession: Session
        @Throws(RepositoryException::class)
        get() {
            if (this.superuser?.isLive != true) {
                this.superuser = this.repository.login(SUPERUSER)
            }
            return this.superuser!!
        }

    /**
     * Retrieve a valid readonly session.
     * @return JCR readonly session
     * @throws RepositoryException
     */
    val readonlySession: Session
        @Throws(RepositoryException::class)
        get() {
            if (this.readonly?.isLive != true) {
                this.readonly = this.repository.login(READONLY)
            }
            return this.readonly!!
        }

    companion object {

        private val SUPERUSER = SimpleCredentials(
                "admin",
                "admin".toCharArray()
        )

        private val READONLY = SimpleCredentials(
                "admin",
                "admin".toCharArray()
        )
    }
}
