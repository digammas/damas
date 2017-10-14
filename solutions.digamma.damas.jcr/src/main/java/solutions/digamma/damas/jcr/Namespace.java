package solutions.digamma.damas.jcr;

/**
 * JCR node and property names.
 *
 * @author Ahmad Shahwan
 */
public interface Namespace {

    /**
     * Namespace delimiter.
     */
    String SEPARATOR = ":";

    /**
     * DMS name space.
     */
    String NAMESPACE = "dms";

    String FILE = prefix("File");

    String COMMENT = prefix("Comment");

    String COMMENT_RECEIVER = prefix("CommentReceiver");

    String RANK = prefix("Rank");

    String DOCUMENT = prefix("Document");

    String FOLDER = prefix("Folder");

    String SUBJECT = prefix("Subject");

    String USER = prefix("User");

    String GROUP = prefix("Group");

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
