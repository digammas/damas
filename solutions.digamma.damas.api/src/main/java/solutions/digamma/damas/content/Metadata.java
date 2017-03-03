package solutions.digamma.damas.content;

/**
 * Metadata object that can be attached to files.
 *
 * @author Ahmad Shahwan
 */
public interface Metadata {

    String getTitle();

    void setTitle(final String value);

    String getDescription();

    void setDescription(final String value);

    String[] getKeywords();

    void setKeywords(final String[] value);
}
