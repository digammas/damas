package solutions.digamma.damas.jcr.sys

import java.security.Principal
import java.security.acl.Group
import java.util.Enumeration
import java.util.Vector

/**
 * System principals. Those are principals that can run a JCR repository as
 * expected by modeshape.
 */
enum class SystemRole(private val principal: String): Principal {

    READONLY("readonly"),
    READWRITE("readwrite"),
    ADMIN("admin");

    private val vector = Vector<Principal>(1)

    override fun getName(): String = this.principal
}