package solutions.digamma.damas.jcr;

import org.mockito.Mockito;
import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.Folder;

/**
 * @author Ahmad Shahwan
 */
public class Mocks {

    public static Document document(String parentId, String name)
            throws WorkspaceException {
        Document document = Mockito.mock(Document.class);
        Mockito.when(document.getParentId()).thenReturn(parentId);
        Mockito.when(document.getName()).thenReturn(name);
        return document;
    }

    public static Folder folder(String parentId, String name)
            throws WorkspaceException {
        Folder folder = Mockito.mock(Folder.class);
        Mockito.when(folder.getParentId()).thenReturn(parentId);
        Mockito.when(folder.getName()).thenReturn(name);
        return folder;
    }

    public static Comment comment(String receiverId, String text, Long rank)
            throws WorkspaceException {
        Comment comment = Mockito.mock(Comment.class);
        Mockito.when(comment.getReceiverId()).thenReturn(receiverId);
        Mockito.when(comment.getText()).thenReturn(text);
        Mockito.when(comment.getRank()).thenReturn(rank);
        return comment;
    }
}
