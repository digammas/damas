package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Metadata;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Metadata object serialization.
 */
@XmlRootElement(name = "Metadata")
public class MetadataSerialization implements Metadata {

    private String title;
    private String description;
    private List<String> keywords;

    /**
     * No-argument constructor.
     */
    public MetadataSerialization() {
    }

    /**
     * Copy constructor.
     *
     * @param pattern   A copy to mimic.
     * @throws          WorkspaceException
     */
    private MetadataSerialization(Metadata pattern) throws WorkspaceException {
        this.title = pattern.getTitle();
        this.description = pattern.getDescription();
        this.keywords = pattern.getKeywords();
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String value) {
        this.title = value;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String value) {
        this.description = value;
    }

    @Override
    public List<String> getKeywords() {
        return this.keywords;
    }

    @Override
    public void setKeywords(List<String> value) {
        this.keywords = value;
    }

    /**
     * Create serializable copy from a pattern.
     *
     * @param p     The pattern.
     * @return      Serializable copy.
     * @throws WorkspaceException
     */
    public static MetadataSerialization from(Metadata p)
            throws WorkspaceException {
        return p == null ? null : new MetadataSerialization(p);
    }
}
