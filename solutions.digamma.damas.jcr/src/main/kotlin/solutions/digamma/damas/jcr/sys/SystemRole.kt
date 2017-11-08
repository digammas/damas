package solutions.digamma.damas.jcr.sys

import java.security.Principal
import java.security.acl.Group
import java.util.Enumeration
import java.util.Vector

enum class SystemRole(private val principal: String): Group {

    READONLY("readonly"),
    READWRITE("readwrite"),
    ADMIN("admin");

    private val vector = Vector<Principal>(1)

    init {
        this.vector.add(this)
    }

    override fun removeMember(user: Principal?): Boolean = false

    override fun getName(): String = this.principal

    override fun addMember(user: Principal?): Boolean = false

    override fun isMember(member: Principal?): Boolean {
        return member?.name == this.name
    }

    override fun members(): Enumeration<out Principal> {
        return this.vector.elements()
    }
}