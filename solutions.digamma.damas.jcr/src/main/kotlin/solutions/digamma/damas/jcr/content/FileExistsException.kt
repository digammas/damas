package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.ConflictException

internal class FileExistsException
internal constructor(path: String, cause: Exception?):
        ConflictException("File at ${strip(path)} already exists.", cause) {

    internal constructor(path: String): this(path, null)

    companion object {

        fun strip(path: String) =
        if (path.startsWith(JcrFile.ROOT_PATH)) {
            path.substring(JcrFile.ROOT_PATH.length)
        } else { path }
    }
}