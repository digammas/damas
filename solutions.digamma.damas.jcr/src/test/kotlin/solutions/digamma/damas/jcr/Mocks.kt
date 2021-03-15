package solutions.digamma.damas.jcr

import org.mockito.Mockito
import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.content.Comment
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.search.Filter
import solutions.digamma.damas.user.Group
import solutions.digamma.damas.user.User

/**
 * @author Ahmad Shahwan
 */
object Mocks {

    fun document(parentId: String? = null, name: String? = null): Document {
        val document = Mockito.mock(Document::class.java)
        Mockito.`when`(document.parentId).thenReturn(parentId)
        Mockito.`when`(document.name).thenReturn(name)
        return document
    }

    fun folder(parentId: String?, name: String?): Folder {
        val folder = Mockito.mock(Folder::class.java)
        Mockito.`when`(folder.parentId).thenReturn(parentId)
        Mockito.`when`(folder.name).thenReturn(name)
        return folder
    }

    fun comment(receiverId: String?, text: String?, rank: Long?): Comment {
        val comment = Mockito.mock(Comment::class.java)
        Mockito.`when`(comment.receiverId).thenReturn(receiverId)
        Mockito.`when`(comment.text).thenReturn(text)
        Mockito.`when`(comment.rank).thenReturn(rank)
        return comment
    }

    fun filter(scopeId: String, recursive: Boolean): Filter {
        val filter = Mockito.mock(Filter::class.java)
        Mockito.`when`(filter.scopeId).thenReturn(scopeId)
        Mockito.`when`(filter.isRecursive).thenReturn(recursive)
        return filter
    }

    fun user(login: String, groups: List<String>): User =
            Mockito.mock(User::class.java).also {
        Mockito.`when`(it.login).thenReturn(login)
        Mockito.`when`(it.memberships).thenReturn(groups)
    }

    fun group(name: String): Group = Mockito.mock(Group::class.java).also {
        Mockito.`when`(it.name).thenReturn(name)
    }

    fun permission(
            objectId: String?,
            subjectId: String,
            accessRights: AccessRight)
                = Mockito.mock(Permission::class.java).also {
        Mockito.`when`(it.subjectId).thenReturn(subjectId)
        Mockito.`when`(it.objectId).thenReturn(objectId)
        Mockito.`when`(it.accessRights).thenReturn(accessRights)
    }

    fun <T> on(getter: T, value: T) {
        Mockito.`when`(getter).thenReturn(value)
    }
}
