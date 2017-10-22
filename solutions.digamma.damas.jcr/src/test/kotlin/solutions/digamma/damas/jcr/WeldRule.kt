package solutions.digamma.damas.jcr

import org.jboss.weld.environment.se.Weld
import org.jboss.weld.environment.se.WeldContainer
import org.junit.rules.ExternalResource

/**
 * CDI container test rule.
 *
 * @author Ahmad Shahwan
 */
/**
 * Constructor.
 */
class WeldRule : ExternalResource() {

    private var container: WeldContainer? = null

    override fun before() {
        /* Initialization is done here so that container can be used by runner.
         */
        this.container = Weld().initialize()
    }

    override fun after() {
        if (this.container != null) {
            this.container!!.close()
        }
    }

    /**
     * Find dependency convert type `klass`.
     *
     * @param klass
     * @param <T>
     * @return
    </T> */
    fun <T> inject(klass: Class<T>): T {
        return container!!.select(klass).get()
    }
}