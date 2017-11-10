package solutions.digamma.damas.jcr.auth

import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.auth.Privilege
import solutions.digamma.damas.content.File
import solutions.digamma.damas.jcr.content.JcrFile
import solutions.digamma.damas.jcr.user.JcrSubject
import solutions.digamma.damas.user.Subject
import java.util.Calendar
import java.util.EnumSet

/**
 * @author Ahmad Shahwan
 */
class JcrPermission private constructor(
        private var file: JcrFile,
        private var subject: JcrSubject
): Permission {

    private var privileges: EnumSet<Privilege> =
            EnumSet.noneOf(Privilege::class.java)

    override fun getModifiedBy(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPrivileges(): EnumSet<Privilege> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getId(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPrivileges(value: EnumSet<Privilege>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSubject(): Subject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setSubjectId(value: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getObject(): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setObjectId(value: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCreatedBy(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCreationDate(): Calendar {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getModificationDate(): Calendar {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
