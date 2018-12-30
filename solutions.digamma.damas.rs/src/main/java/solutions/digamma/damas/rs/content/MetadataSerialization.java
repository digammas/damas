package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Metadata;

import java.util.List;

/**
 * Metadata object serialization.
 */
public class MetadataSerialization implements Metadata {

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setTitle(String value) throws WorkspaceException {

    }

    @Override
    public String getDescription() throws WorkspaceException {
        return null;
    }

    @Override
    public void setDescription(String value) throws WorkspaceException {

    }

    @Override
    public List<String> getKeywords() throws WorkspaceException {
        return null;
    }

    @Override
    public void setKeywords(List<String> value) throws WorkspaceException {

    }
}
