package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.fail.JcrException;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.Calendar;

/**
 * Entity implementation, with underling JCR node.
 *
 * If the value (the second argument) passed to a setter is not null property
 * will be updated or created with this value. Otherwise, property will be
 * removed if it exists.
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
    @Nonnull
    Node getNode();

    @Nonnull
    default String getId() throws DocumentException {
        try {
            return this.getNode().getIdentifier();
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    /**
     * String property.
     *
     * @param name Property name
     * @return Property value.
     * @throws DocumentException
     */
    default @Nonnull String getString(@Nonnull String name)
            throws DocumentException {
        try {
            return this.getNode().getProperty(name).getString();
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    /**
     * Set or remove a string property.
     *
     * @param name Property name.
     * @param value Property new value.
     * @throws DocumentException
     */
    default void setString(@Nonnull String name, @Nullable String value)
            throws DocumentException{
        try {
            Property property = this.getNode().setProperty(name, value);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }


    /**
     * Date property.
     *
     * @param name Property name.
     * @return Property value.
     * @throws DocumentException
     */
    default @Nonnull Calendar getDate(@Nonnull String name)
            throws DocumentException {
        try {
            return this.getNode().getProperty(name).getDate();
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    /**
     * Set or remove a date property.
     *
     * @param name Property name.
     * @param value Property new value.
     * @throws DocumentException
     */
    default void setDate(@Nonnull String name, @Nullable Calendar value)
            throws DocumentException{
        try {
            Property property = this.getNode().setProperty(name, value);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    /**
     * Integer property.
     *
     * @param name Property name.
     * @return Property value.
     * @throws DocumentException
     */
    default @Nonnull Long getLong(@Nonnull String name)
            throws DocumentException {
        try {
            return this.getNode().getProperty(name).getLong();
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    /**
     * Set or remove integer property.
     *
     * @param name Property name.
     * @param value Property new value.
     * @throws DocumentException
     */
    default void setLong(@Nonnull String name, @Nullable Long value)
            throws DocumentException{
        try {
            Property property = this.getNode().setProperty(name, value);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

}
