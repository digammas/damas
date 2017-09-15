package solutions.digamma.damas.jcr;

import org.mockito.Mockito;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.Folder;

/**
 * @author Ahmad Shahwan
 */
public class Mocks {

    public static Document document(String parentId, String name)
            throws DocumentException {
        Document document = Mockito.mock(Document.class);
        Mockito.when(document.getParentId()).thenReturn(parentId);
        Mockito.when(document.getName()).thenReturn(name);
        return document;
    }

    public static Folder folder(String parentId, String name)
            throws DocumentException {
        Folder folder = Mockito.mock(Folder.class);
        Mockito.when(folder.getParentId()).thenReturn(parentId);
        Mockito.when(folder.getName()).thenReturn(name);
        return folder;
    }
}
