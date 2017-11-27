package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.InternalStateException

/**
 * An exception denoting confused node type. This is an internal state exception
 * and should be fully logged.
 */
class InvalidNodeTypeException: InternalStateException {

    constructor(path: String, expected: String):
            super("Node at $path is not of expected type $expected.")

    constructor(path: String): super("Invalid node type at $path.")
}