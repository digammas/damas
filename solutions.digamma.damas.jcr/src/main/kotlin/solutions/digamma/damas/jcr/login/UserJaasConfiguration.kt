package solutions.digamma.damas.jcr.login

import solutions.digamma.damas.jaas.JaasConfiguration
import solutions.digamma.damas.jaas.Realm
import java.util.Collections
import javax.inject.Singleton
import javax.security.auth.login.AppConfigurationEntry
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag
import javax.security.auth.login.Configuration

/**
 * JAAS configuration for user authentication.
 *
 * Unlike `solutions.digamma.damas.jcr.sys.SystemJaasConfiguration` that
 * authenticate system users, this configuration is responsible of
 * authenticating non-system users created by the user management module.
 */
@Singleton
@Realm(UserJaasConfiguration.REALM, JaasConfiguration.REALM)
internal class UserJaasConfiguration : Configuration() {

    private val entries = Array(1) {
        AppConfigurationEntry(
                UserLoginModule::class.java.name,
                LoginModuleControlFlag.SUFFICIENT,
                Collections.emptyMap<String, Any>())
    }

    override fun getAppConfigurationEntry(name: String):
            Array<AppConfigurationEntry> {
        return if (name in arrayOf(REALM, JaasConfiguration.REALM))
            this.entries else emptyArray()
    }

    companion object {

        /**
         * User realm.
         */
        internal const val REALM: String = "damas-user"
    }
}
