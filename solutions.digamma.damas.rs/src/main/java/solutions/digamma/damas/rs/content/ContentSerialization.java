package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.Folder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Content")
public class ContentSerialization implements Folder.Content {

    private List<FolderSerialization> folders;
    private List<DocumentSerialization> documents;

    public ContentSerialization() {
    }

    private ContentSerialization(Folder.Content clone) {
        this.folders = FolderSerialization.from(clone.getFolders());
        this.documents = DocumentSerialization.from(clone.getDocuments());
    }

    @Override
    public List<FolderSerialization> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderSerialization> folders) {
        this.folders = folders;
    }

    @Override
    public List<DocumentSerialization> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentSerialization> documents) {
        this.documents = documents;
    }

    public static ContentSerialization from(Folder.Content clone) {
        return clone == null ? null : new ContentSerialization(clone);
    }
}
