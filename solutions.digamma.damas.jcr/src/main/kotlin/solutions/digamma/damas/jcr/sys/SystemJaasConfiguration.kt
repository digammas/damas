package solutions.digamma.damas.jcr.sys

import solutions.digamma.damas.jaas.Realm
import java.util.Collections
import javax.inject.Singleton
import javax.security.auth.login.AppConfigurationEntry
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag
import javax.security.auth.login.Configuration

/**
 * JAAS configuration for system authentication.
 */
@Singleton
@Realm(SystemJaasConfiguration.REALM)
class SystemJaasConfiguration : Configuration() {

    private val entries = Array(1) {
        AppConfigurationEntry(
                SystemLoginModule::class.java.name,
                LoginModuleControlFlag.SUFFICIENT,
                Collections.emptyMap<String, Any>())
    }

    override fun getAppConfigurationEntry(name: String):
            Array<AppConfigurationEntry> {
        return if (name == REALM) this.entries else emptyArray()
    }

    companion object {

        internal const val REALM: String = "damas-system"
    }
}