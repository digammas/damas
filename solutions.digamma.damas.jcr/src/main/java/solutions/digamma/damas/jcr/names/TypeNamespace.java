package solutions.digamma.damas.jcr.names;

/**
 * JCR domain-specific node type names.
 *
 * @author Ahmad Shahwan
 */
public interface TypeNamespace extends Namespace {

    String FILE = Namespace.prefix("File");

    String COMMENT = Namespace.prefix("Comment");

    String COMMENT_RECEIVER = Namespace.prefix("CommentReceiver");

    String DOCUMENT = Namespace.prefix("Document");

    String FOLDER = Namespace.prefix("Folder");

    String SUBJECT = Namespace.prefix("Subject");

    String USER = Namespace.prefix("User");

    String GROUP = Namespace.prefix("Group");
}
