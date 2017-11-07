package solutions.digamma.damas.jcr.sys

import solutions.digamma.damas.jcr.auth.UserLoginModule
import java.util.Arrays
import javax.security.auth.login.LoginException

/**
 * @author Ahmad Shahwan
 */
internal class SystemLoginModule : UserLoginModule() {

    @Throws(LoginException::class)
    override fun login(): Boolean {
        if (SystemSessions.SU_USERNAME == this.login &&
                Arrays.equals(SystemSessions.SU_PASSWORD, this.password)) {
            this.roles = listOf("readwrite", "admin")
            return true
        }
        if (SystemSessions.RO_USERNAME == this.login &&
                Arrays.equals(SystemSessions.RO_PASSWORD, this.password)) {
            this.roles = listOf("readonly")
            return true
        }
        return false
    }
}
