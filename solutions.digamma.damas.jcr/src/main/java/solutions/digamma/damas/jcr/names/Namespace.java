package solutions.digamma.damas.jcr.names;

/**
 * Namespace constants and methods.
 *
 * @author Ahmad Shahwan
 */
public interface Namespace {

    /**
     * TypeNamespace delimiter.
     */
    String SEPARATOR = ":";

    /**
     * DMS name space.
     */
    String NAMESPACE = "dms";

    /**
     * Prefix name with namespace.
     *
     * @param name
     * @return
     */
    static String prefix(String name) {
        return String.format("%s%s%s", NAMESPACE, SEPARATOR, name);
    }
}
