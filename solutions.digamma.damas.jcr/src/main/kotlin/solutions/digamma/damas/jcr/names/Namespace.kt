package solutions.digamma.damas.jcr.names

/**
 * Namespace constants and methods.
 *
 * @author Ahmad Shahwan
 */
interface Namespace {
    companion object {

        /**
         * TypeNamespace delimiter.
         */
        val SEPARATOR = ":"

        /**
         * DMS name space.
         */
        val NAMESPACE = "dms"

        /**
         * Prefix name with namespace.
         *
         * @param name
         * @return
         */
        fun prefix(name: String): String {
            return String.format("%s%s%s", NAMESPACE, SEPARATOR, name)
        }
    }
}
