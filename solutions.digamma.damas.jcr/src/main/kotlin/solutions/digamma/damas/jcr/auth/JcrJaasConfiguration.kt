package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.jaas.Realm
import java.util.Collections
import javax.inject.Singleton
import javax.security.auth.login.AppConfigurationEntry
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag
import javax.security.auth.login.Configuration

/**
 * Add new entry to JAAS configuration.
 */
@Singleton
@Realm(JcrJaasConfiguration.REALM)
class JcrJaasConfiguration: Configuration() {

    private val entries =  Array(1, {
        AppConfigurationEntry(
                JcrLoginModule::class.java.name,
                LoginModuleControlFlag.REQUISITE,
                Collections.emptyMap<String, Any>())
    })

    override fun getAppConfigurationEntry(name: String):
            Array<AppConfigurationEntry> {
        return if (name == REALM) this.entries else emptyArray()
    }

    companion object {

        const val REALM: String = "damas"
    }
}