package solutions.digamma.damas.jcr.session

import solutions.digamma.damas.common.AuthenticationException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.session.Transaction
import solutions.digamma.damas.session.TransactionManager
import solutions.digamma.damas.login.Token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal open class JcrTransactionManager : TransactionManager {

    @Inject
    private lateinit var bookkeepr: SessionBookkeeper

    @Throws(WorkspaceException::class)
    override fun begin(token: Token?): Transaction {
        try {
            return JcrTransaction(this.bookkeepr.lookup(token))
        } catch (e: NotFoundException) {
            throw AuthenticationException(e)
        }
    }
}