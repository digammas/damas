package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.error.JcrException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Calendar;

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
    default String getId() throws DocumentException {
        try {
            return this.getNode().getIdentifier();
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    /**
     * String property.
     *
     * @param name Property name
     * @return Property value.
     * @throws DocumentException
     */
    default @NotNull String getString(@NotNull String name)
            throws DocumentException {
        try {
            return this.getNode().getProperty(name).getString();
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    /**
     * Set or remove a string property.
     *
     * If {@code value} is not null property will be updated or created with
     * this value. Otherwise ({@code value} is null), property will be removed
     * if it exists.
     *
     * @param name Property name.
     * @param value Property new value.
     * @throws DocumentException
     */
    default void setString(@NotNull String name, @Nullable String value)
            throws DocumentException{
        try {
            this.getNode().setProperty(name, value);
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }


    /**
     * Date property.
     *
     * @param name Property name.
     * @return Property value.
     * @throws DocumentException
     */
    default @NotNull Calendar getDate(@NotNull String name)
            throws DocumentException {
        try {
            return this.getNode().getProperty(name).getDate();
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    /**
     * Set or remove a date property.
     *
     * If {@code value} is not null property will be updated or created with
     * this value. Otherwise ({@code value} is null), property will be removed
     * if it exists.
     *
     * @param name Property name.
     * @param value Property new value.
     * @throws DocumentException
     */
    default void setDate(@NotNull String name, @Nullable Calendar value)
            throws DocumentException{
        try {
            this.getNode().setProperty(name, value);
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    /**
     * Integer property.
     *
     * @param name Property name.
     * @return Property value.
     * @throws DocumentException
     */
    default @NotNull Long getLong(@NotNull String name)
            throws DocumentException {
        try {
            return this.getNode().getProperty(name).getLong();
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    /**
     * Set or remove integer property.
     *
     * If {@code value} is not null property will be updated or created with
     * this value. Otherwise ({@code value} is null), property will be removed
     * if it exists.
     *
     * @param name Property name.
     * @param value Property new value.
     * @throws DocumentException
     */
    default void setLong(@NotNull String name, @Nullable Long value)
            throws DocumentException{
        try {
            this.getNode().setProperty(name, value);
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

}
