package solutions.digamma.damas.jcr

import org.mockito.Mockito
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Comment
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.Folder

/**
 * @author Ahmad Shahwan
 */
object Mocks {

    @Throws(WorkspaceException::class)
    fun document(parentId: String?, name: String?): Document {
        val document = Mockito.mock(Document::class.java)
        Mockito.`when`(document.parentId).thenReturn(parentId)
        Mockito.`when`(document.name).thenReturn(name)
        return document
    }

    @Throws(WorkspaceException::class)
    fun folder(parentId: String?, name: String?): Folder {
        val folder = Mockito.mock(Folder::class.java)
        Mockito.`when`(folder.parentId).thenReturn(parentId)
        Mockito.`when`(folder.name).thenReturn(name)
        return folder
    }

    @Throws(WorkspaceException::class)
    fun comment(receiverId: String?, text: String?, rank: Long?): Comment {
        val comment = Mockito.mock(Comment::class.java)
        Mockito.`when`(comment.receiverId).thenReturn(receiverId)
        Mockito.`when`(comment.text).thenReturn(text)
        Mockito.`when`(comment.rank).thenReturn(rank)
        return comment
    }
}
