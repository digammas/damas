package solutions.digamma.damas.jcr.sys

import solutions.digamma.damas.auth.JaasConfiguration
import solutions.digamma.damas.auth.Realm
import java.util.Collections
import javax.annotation.Priority
import javax.inject.Singleton
import javax.security.auth.login.AppConfigurationEntry
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag
import javax.security.auth.login.Configuration

/**
 * JAAS configuration for system authentication.
 */
@Singleton
@Priority(0)
@Realm(SystemJaasConfiguration.REALM, JaasConfiguration.APPLICATION_REALM)
class SystemJaasConfiguration : Configuration() {

    private val entries = Array(1) {
        AppConfigurationEntry(
                SystemLoginModule::class.java.name,
                LoginModuleControlFlag.SUFFICIENT,
                Collections.emptyMap<String, Any>())
    }

    override fun getAppConfigurationEntry(name: String):
            Array<AppConfigurationEntry> {
        return if (name in arrayOf(REALM, JaasConfiguration.APPLICATION_REALM))
            this.entries else emptyArray()
    }

    companion object {

        internal const val REALM: String = "damas-system"
    }
}
