package solutions.digamma.damas.jcr

import org.mockito.Mockito
import solutions.digamma.damas.auth.AccessRight
import solutions.digamma.damas.auth.Permission
import solutions.digamma.damas.content.Comment
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.user.User
import java.util.EnumSet

/**
 * @author Ahmad Shahwan
 */
object Mocks {

    fun document(parentId: String?, name: String?): Document {
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

    fun user(login: String): User {
        return Mockito.mock(User::class.java).also {
            Mockito.`when`(it.login).thenReturn(login)
        }
    }

    fun permission(
            objectId: String,
            subjectId: String,
            accessRights: EnumSet<AccessRight>)
                = Mockito.mock(Permission::class.java).also {
        Mockito.`when`(it.subjectId).thenReturn(subjectId)
        Mockito.`when`(it.objectId).thenReturn(objectId)
        Mockito.`when`(it.accessRights).thenReturn(accessRights)
    }
}
