package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.entity.Created;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.inspection.NotNull;

import javax.jcr.Property;
import java.util.Calendar;

/**
 * JCR creature.
 *
 * @author Ahmad Shahwan
 */
public interface JcrCreated extends Created, JcrEntity {

    @NotNull
    @Override
    default Calendar getCreationDate() throws WorkspaceException {
        return this.getDate(Property.JCR_CREATED);
    }

    @NotNull
    @Override
    default String getCreatedBy() throws WorkspaceException {
        return this.getString(Property.JCR_CREATED_BY);
    }
}
