package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.Created;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.inspection.Nonnull;

import javax.jcr.Property;
import java.util.Calendar;

/**
 * JCR creature.
 *
 * @author Ahmad Shahwan
 */
public interface JcrCreated extends Created, JcrEntity {

    @Nonnull
    @Override
    default Calendar getCreationDate() throws DocumentException {
        return this.getDate(Property.JCR_CREATED);
    }

    @Nonnull
    @Override
    default String getCreatedBy() throws DocumentException {
        return this.getString(Property.JCR_CREATED_BY);
    }
}
