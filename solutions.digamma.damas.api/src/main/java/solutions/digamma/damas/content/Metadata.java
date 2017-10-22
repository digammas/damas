package solutions.digamma.damas.content;

import java.util.List;

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

    List<String> getKeywords();

    void setKeywords(final List<String> value);
}
