package solutions.digamma.damas.search;

import java.time.ZonedDateTime;

/**
 * Search query object.
 */
public interface Filter {

    /**
     * Search scope.
     *
     * Search scope defines where to search. For documents and directory it is a
     * directory ID. For comments, it is a comment receiver ID. For users it is
     * a group ID.
     *
     * @return          search scope's entity ID.
     */
    String getScopeId();

    /**
     * Set search scope.
     *
     * @param value     search scope's entity ID.
     */
    void setScopeId(String value);

    /**
     * Should search be recursive.
     *
     * If search is recursive, scope is searched recursively (including child
     * elements) at an infinite depth.
     *
     * @return          whether search should be recursive.
     */
    Boolean isRecursive();

    /**
     * Set search recursion.
     *
     * @param value     whether search should be recursive.
     */
    void setRecursive(Boolean value);

    /**
     * Name pattern to be search.
     *
     * @return      name pattern.
     */
    String getNamePattern();

    /**
     * Set name pattern.
     *
     * @param value name pattern.
     */
    void setNamePattern(String value);

    /**
     * Created-before date criterion.
     *
     * @return
     */
    ZonedDateTime getCreatedBefore();

    /**
     * Set created-before date criterion.
     *
     * When set, all search results must be created after the provided
     * instance.
     *
     * @param value
     */
    void setCreatedBefore(ZonedDateTime value);

    /**
     * Created-after data criterion.
     *
     * @return
     */
    ZonedDateTime getCreatedAfter();

    /**
     * Set created-after date criterion.
     *
     * When set, all search results must be created strictly before the provided
     * instance.
     *
     * @param value
     */
    void setCreatedAfter(ZonedDateTime value);

    /**
     * Last-modified-before search criterion.
     * 
     * @return
     */
    ZonedDateTime getLastModifiedBefore();

    /**
     * Set last-modified-before search criterion.
     *
     * When set, all search results must have been last modified before the
     * provided date.
     *
     * @param value
     */
    void setLastModifiedBefore(ZonedDateTime value);

    /**
     * Last-modified-before search criterion.
     *
     * @return
     */
    ZonedDateTime getLastModifiedAfter();

    /**
     * Set last-modified-before search criterion.
     *
     * When set, all search results must have been last modified strictly after
     * the provided date.
     *
     * @param value
     */
    void setLastModifiedAfter(ZonedDateTime value);

    /**
     * Created-by search criterion.
     * 
     * @return
     */
    String getCreatedBy();

    /**
     * Set Created-by search criterion.
     *
     * @param value
     */
    void setCreatedBy(String value);

    /**
     * Modified-by seahrc criterion.
     *
     * @return
     */
    String getLastModifiedBy();

    /**
     * Set modified-by seahrc criterion.
     *
     * @param value
     */
    void setLastModifiedBy(String value);
}
