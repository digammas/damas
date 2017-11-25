package solutions.digamma.damas.jcr.sys

import java.security.Principal

/**
 * System principals. Those are principals that can run a JCR repository.
 */
enum class SystemRole(private val principal: String): Principal {

    READONLY("readonly"),
    READWRITE("readwrite"),
    ADMIN("admin");

    override fun getName(): String = this.principal
}