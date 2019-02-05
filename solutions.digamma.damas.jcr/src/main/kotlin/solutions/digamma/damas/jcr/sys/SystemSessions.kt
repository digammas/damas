package solutions.digamma.damas.jcr.sys

import java.util.Base64
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton
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
internal class SystemSessions
@Inject
constructor(private val repository: Repository) {

    private var superuserSession: Session? = null
    private var readonlySession: Session? = null

    /**
     * Retrieve a valid superuser session.
     *
     * @return JCR superuser session
     * @throws RepositoryException
     */
    val superuser: Session
        @Throws(RepositoryException::class)
        get() {
            if (this.superuserSession?.isLive != true) {
                this.superuserSession = this.repository.login(SUPERUSER)
            }
            return this.superuserSession!!
        }

    /**
     * Retrieve a valid readonly session.
     * @return JCR readonly session
     * @throws RepositoryException
     */
    val readonly: Session
        @Throws(RepositoryException::class)
        get() {
            if (this.readonlySession?.isLive != true) {
                this.readonlySession = this.repository.login(READONLY)
            }
            return this.readonlySession!!
        }

    companion object {

        const val SU_USERNAME = "sys:superuser"
        const val RO_USERNAME = "sys:readonly"
        val SU_PASSWORD = generateRandomPassword()
        val RO_PASSWORD = generateRandomPassword()

        private val SUPERUSER = SimpleCredentials(
                SU_USERNAME,
                SU_PASSWORD
        )

        private val READONLY = SimpleCredentials(
                RO_USERNAME,
                RO_PASSWORD
        )

        private fun generateRandomPassword(): CharArray {
            val password = ByteArray(64)
            Random().nextBytes(password)
            return Base64.getEncoder().encodeToString(password).toCharArray()
        }
    }
}
