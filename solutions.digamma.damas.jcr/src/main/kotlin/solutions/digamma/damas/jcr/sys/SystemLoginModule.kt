package solutions.digamma.damas.jcr.sys

import solutions.digamma.damas.jaas.PasswordBasedLoginModule
import solutions.digamma.damas.jcr.auth.UserLoginModule
import java.security.Principal
import java.util.Arrays
import javax.security.auth.login.LoginException

/**
 * @author Ahmad Shahwan
 */
internal class SystemLoginModule : PasswordBasedLoginModule() {

    private val roles: MutableList<Principal> = ArrayList()

    @Throws(LoginException::class)
    override fun login(): Boolean {
        if (SystemSessions.SU_USERNAME == this.login &&
                Arrays.equals(SystemSessions.SU_PASSWORD, this.password)) {
            this.roles.add(SystemRole.READWRITE)
            return true
        }
        if (SystemSessions.RO_USERNAME == this.login &&
                Arrays.equals(SystemSessions.RO_PASSWORD, this.password)) {
            this.roles.add(SystemRole.READONLY)
            return true
        }
        return false
    }

    @Throws(LoginException::class)
    override fun commit(): Boolean {
        val principals = this.subject?.principals
        principals?.addAll(this.roles)
        return true
    }
}
