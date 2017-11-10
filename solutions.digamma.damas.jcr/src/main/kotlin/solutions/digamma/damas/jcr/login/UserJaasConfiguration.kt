package solutions.digamma.damas.jcr.login

import solutions.digamma.damas.jaas.Realm
import java.util.Collections
import javax.inject.Singleton
import javax.security.auth.login.AppConfigurationEntry
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag
import javax.security.auth.login.Configuration

/**
 * JAAS configuration for user authentication.
 */
@Singleton
@Realm(UserJaasConfiguration.REALM)
internal class UserJaasConfiguration : Configuration() {

    private val entries =  Array(1, {
        AppConfigurationEntry(
                UserLoginModule::class.java.name,
                LoginModuleControlFlag.REQUISITE,
                Collections.emptyMap<String, Any>())
    })

    override fun getAppConfigurationEntry(name: String):
            Array<AppConfigurationEntry> {
        return if (name == REALM) this.entries else emptyArray()
    }

    companion object {

        const val REALM: String = "damas-user"
    }
}