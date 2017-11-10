package solutions.digamma.damas.auth;

/**
 * Access privilege.
 * An access privilege can be granted to a given subject, on given objects.
 * Subjects are users and groups. Objects can be a document or a folder.
 *
 * @author Ahmad Shahwan
 */
public enum Privilege {
    READ,
    WRITE,
    SHARE;
}
