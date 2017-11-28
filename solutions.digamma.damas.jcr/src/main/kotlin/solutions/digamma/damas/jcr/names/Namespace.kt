package solutions.digamma.damas.jcr.names

/**
 * Namespace constants and methods.
 *
 * @author Ahmad Shahwan
 */
internal object Namespace {

    /**
     * TypeNamespace delimiter.
     */
    private const val SEPARATOR = ":"

    /**
     * DMS name space.
     */
    private const val NAMESPACE = "dms"

    /**
     * Prefix name with namespace.
     *
     * @param name
     * @return
     */
    fun prefix(name: String): String {
        return "%s%s%s".format(NAMESPACE, SEPARATOR, name)
    }
}
