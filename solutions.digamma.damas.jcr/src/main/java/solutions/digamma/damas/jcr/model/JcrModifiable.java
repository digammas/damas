package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Modifiable;
import solutions.digamma.damas.inspection.Nonnull;

import javax.jcr.Property;
import java.util.Calendar;

/**
 * JCR modifiable.
 *
 * @author Ahmad Shahwan
 */
public interface JcrModifiable extends Modifiable, JcrEntity {

    /**
     * Login with which last modification was done.
     *
     * @return
     */
    @Override
    @Nonnull
    default String getModifiedBy() throws DocumentException {
        return this.getString(Property.JCR_LAST_MODIFIED_BY);
    }

    /**
     * Last modification timestamp.
     *
     * @return
     */
    @Override
    @Nonnull
    default Calendar getModificationDate() throws DocumentException {
        return this.getDate(Property.JCR_LAST_MODIFIED);
    }
}
