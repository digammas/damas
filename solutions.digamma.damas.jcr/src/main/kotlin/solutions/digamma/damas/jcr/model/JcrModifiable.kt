package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Modifiable
import solutions.digamma.damas.inspection.NotNull

import javax.jcr.Property
import java.util.Calendar

/**
 * JCR modifiable.
 *
 * @author Ahmad Shahwan
 */
interface JcrModifiable : Modifiable, JcrEntity {

    /**
     * Login with which last modification was done.
     *
     * @return
     */
    @Throws(WorkspaceException::class)
    override fun getModifiedBy() =
            this.getString(Property.JCR_LAST_MODIFIED_BY)

    /**
     * Last modification timestamp.
     *
     * @return
     */
    @Throws(WorkspaceException::class)
    override fun getModificationDate() =
            this.getDate(Property.JCR_LAST_MODIFIED)
}
