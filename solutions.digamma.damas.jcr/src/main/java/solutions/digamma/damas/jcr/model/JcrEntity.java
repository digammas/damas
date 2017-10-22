package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.common.Exceptions;
import solutions.digamma.damas.jcr.names.ItemNamespace;
import solutions.digamma.damas.jcr.names.TypeNamespace;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity implementation, with underling JCR node.
 *
 * @author Ahmad Shahwan
 */
public interface JcrEntity extends Entity {

    String SQL2_SELECT_CHILDREN =
            "SELECT * FROM [%s] AS c WHERE ISCHILDNODE(c, %s)";

    /**
     * Underling JCR node.
     *
     * @return
     */
    @NotNull Node getNode();

    @NotNull
    default String getId() throws WorkspaceException {
        return Exceptions.wrap(() -> this.getNode().getIdentifier());
    }

    /**
     * String property.
     *
     * @param name Property name
     * @return Property value
     * @throws WorkspaceException
     */
    default @NotNull String getString(@NotNull String name)
            throws WorkspaceException {
        return Exceptions.wrap(() ->
                this.getNode().getProperty(name).getString());
    }

    /**
     * Set or remove a string property.
     *
     * If {@code value} is not null property will be updated or created with
     * this value. Otherwise ({@code value} is null), property will be removed
     * if it exists.
     *
     * @param name Property name
     * @param value Property new value
     * @throws WorkspaceException
     */
    default void setString(@NotNull String name, @Nullable String value)
            throws WorkspaceException {
        Exceptions.wrap(() -> this.getNode().setProperty(name, value));
    }

    /**
     * Llist of all string values of a property.
     *
     * @param name                  Property name
     * @return
     * @throws WorkspaceException
     */
    default List<String> getStrings(String name) throws WorkspaceException {
        return Exceptions.wrap(() -> Arrays.stream(this
                .getNode()
                .getProperty(name)
                .getValues())
            .map(x -> Exceptions.mute(x::getString))
            .collect(Collectors.toList()));
    }


    /**
     * Date property.
     *
     * @param name Property name
     * @return Property value
     * @throws WorkspaceException
     */
    default @NotNull Calendar getDate(@NotNull String name)
            throws WorkspaceException {
        return Exceptions.wrap(() ->
                this.getNode().getProperty(name).getDate());
    }

    /**
     * Set or remove a date property.
     *
     * If {@code value} is not null property will be updated or created with
     * this value. Otherwise ({@code value} is null), property will be removed
     * if it exists.
     *
     * @param name Property name
     * @param value Property new value
     * @throws WorkspaceException
     */
    default void setDate(@NotNull String name, @Nullable Calendar value)
            throws WorkspaceException {
        Exceptions.wrap(() -> this.getNode().setProperty(name, value));
    }

    /**
     * Integer property.
     *
     * @param name Property name
     * @return Property value
     * @throws WorkspaceException
     */
    default @NotNull Long getLong(@NotNull String name)
            throws WorkspaceException {
        return Exceptions.wrap(() ->
                this.getNode().getProperty(name).getLong());
    }

    /**
     * Set or remove integer property.
     *
     * If {@code value} is not null property will be updated or created with
     * this value. Otherwise ({@code value} is null), property will be removed
     * if it exists.
     *
     * @param name Property name
     * @param value Property new value
     * @throws WorkspaceException
     */
    default void setLong(@NotNull String name, @Nullable Long value)
            throws WorkspaceException {
        Exceptions.wrap(() -> this.getNode().setProperty(name, value));
    }

    /**
     * Retrieve an iterator over child nodes of a certain type.
     *
     * @param type
     * @return
     * @throws RepositoryException
     */
    default NodeIterator getChildNodes(String type) throws RepositoryException {
        String path = this.getNode().getPath();
        String sql2 = String.format(SQL2_SELECT_CHILDREN, type, path);
        QueryManager manager = this.getNode()
                .getSession().getWorkspace().getQueryManager();
        Query query = manager.createQuery(sql2, Query.JCR_SQL2);
        QueryResult result = query.execute();
        return result.getNodes();
    }

}
