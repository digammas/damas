package solutions.digamma.damas.jcr.names

/**
 * JCR distinguished nodes and properties names.
 *
 * @author Ahmad Shahwan
 */
interface ItemNamespace {
    companion object {

        val RANK = Namespace.prefix("rank")

        val GROUPS = Namespace.prefix("groups")

        val LOGIN = Namespace.prefix("login")

        val PASSWORD = Namespace.prefix("password")

        val ALIAS = Namespace.prefix("alias")

        val EMAIL = Namespace.prefix("mail")

        val FIRST_NAME = Namespace.prefix("firstName")

        val LAST_NAME = Namespace.prefix("lastName")
    }
}
