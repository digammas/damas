package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.Modifiable;
import solutions.digamma.damas.inspection.NotNull;

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
    @NotNull
    default String getModifiedBy() throws WorkspaceException {
        return this.getString(Property.JCR_LAST_MODIFIED_BY);
    }

    /**
     * Last modification timestamp.
     *
     * @return
     */
    @Override
    @NotNull
    default Calendar getModificationDate() throws WorkspaceException {
        return this.getDate(Property.JCR_LAST_MODIFIED);
    }
}
