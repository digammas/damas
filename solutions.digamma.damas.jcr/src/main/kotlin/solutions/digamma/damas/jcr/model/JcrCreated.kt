package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.entity.Created
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.inspection.NotNull

import javax.jcr.Property
import java.util.Calendar

/**
 * JCR creature.
 *
 * @author Ahmad Shahwan
 */
interface JcrCreated : Created, JcrEntity {


    @Throws(WorkspaceException::class)
    override fun getCreationDate(): Calendar {
        return this.getDate(Property.JCR_CREATED)
    }


    @Throws(WorkspaceException::class)
    override fun getCreatedBy(): String {
        return this.getString(Property.JCR_CREATED_BY)
    }
}
