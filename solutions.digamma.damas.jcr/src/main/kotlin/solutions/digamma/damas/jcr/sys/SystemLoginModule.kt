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
            this.roles.add("readwrite")
            this.roles.add("admin")
            return true
        }
        if (SystemSessions.RO_USERNAME == this.login &&
                Arrays.equals(SystemSessions.RO_PASSWORD, this.password)) {
            this.roles.add("readonly")
            return true
        }
        return false
    }
}
