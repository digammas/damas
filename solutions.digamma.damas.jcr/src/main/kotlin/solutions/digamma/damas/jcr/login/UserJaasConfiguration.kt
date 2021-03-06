package solutions.digamma.damas.jcr.login

import solutions.digamma.damas.auth.JaasConfiguration
import solutions.digamma.damas.auth.Realm
import java.util.Collections
import javax.inject.Singleton
import javax.security.auth.login.AppConfigurationEntry
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag
import javax.security.auth.login.Configuration

/**
 * JAAS configuration for user authentication.
 *
 * Unlike [solutions.digamma.damas.jcr.sys.SystemJaasConfiguration] that
 * authenticate system users, this configuration is responsible of
 * authenticating non-system users created by the user management module.
 */
@Singleton
@Realm(UserJaasConfiguration.REALM, JaasConfiguration.APPLICATION_REALM)
internal class UserJaasConfiguration : Configuration() {

    private val entries = Array(1) {
        AppConfigurationEntry(
                UserLoginModule::class.java.name,
                LoginModuleControlFlag.SUFFICIENT,
                Collections.emptyMap<String, Any>())
    }

    override fun getAppConfigurationEntry(name: String):
            Array<AppConfigurationEntry> {
        return if (name in arrayOf(REALM, JaasConfiguration.APPLICATION_REALM))
            this.entries else emptyArray()
    }

    companion object {

        /**
         * User realm.
         */
        internal const val REALM: String = "damas-user"
    }
}
