package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;

import java.util.List;

/**
 * Metadata object that can be attached to files.
 *
 * @author Ahmad Shahwan
 */
public interface Metadata {

    String getTitle();

    void setTitle(final String value) throws WorkspaceException;

    String getDescription() throws WorkspaceException;

    void setDescription(final String value) throws WorkspaceException;

    List<String> getKeywords() throws WorkspaceException;

    void setKeywords(final List<String> value) throws WorkspaceException;
}
