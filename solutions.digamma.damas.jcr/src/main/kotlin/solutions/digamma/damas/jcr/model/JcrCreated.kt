package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Created
import javax.jcr.Property

/**
 * JCR creature.
 *
 * @author Ahmad Shahwan
 */
internal interface JcrCreated : Created, JcrEntity {


    @Throws(WorkspaceException::class)
    override fun getCreationDate() = this.getDate(Property.JCR_CREATED)

    @Throws(WorkspaceException::class)
    override fun getCreatedBy() = this.getString(Property.JCR_CREATED_BY)
}
