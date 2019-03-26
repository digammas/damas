package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.entity.Modifiable

import javax.jcr.Property

/**
 * JCR modifiable.
 *
 * @author Ahmad Shahwan
 */
internal interface JcrModifiable : Modifiable, JcrEntity {

    /**
     * Login with which last modification was done.
     *
     * @return
     */
    override fun getModifiedBy() =
            this.getString(Property.JCR_LAST_MODIFIED_BY)

    /**
     * Last modification timestamp.
     *
     * @return
     */
    override fun getModificationDate() =
            this.getDate(Property.JCR_LAST_MODIFIED)
}
