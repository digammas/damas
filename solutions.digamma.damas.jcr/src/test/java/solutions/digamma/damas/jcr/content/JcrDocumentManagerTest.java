package solutions.digamma.damas.jcr.content;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.jcr.Mocks;
import solutions.digamma.damas.jcr.WeldTest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Test scenarios for {@link JcrDocumentManager}.
 *
 * Created by Ahmad on 9/2/17.
 */
public class JcrDocumentManagerTest extends WeldTest {

    private DocumentManager manager;
    private FolderManager folderManager;
    private Token token;

    @Before
    public void setUp() throws Exception {
        this.manager = inject(JcrDocumentManager.class);
        this.folderManager = inject(FolderManager.class);
        this.token = this.login.login("admin", "admin");

    }

    @After
    public void tearDown() throws Exception {
        this.login.logout(this.token);
    }


    @Test
    public void create() throws Exception {
        final String name = "test.txt";
        String rootId = this.folderManager
                .find(this.token).getObjects().iterator().next().getId();
        Document document =
                manager.create(this.token, Mocks.document(rootId, name));
        assert name.equals(document.getName()) : "Document name mismatch";
        assert rootId.equals(document.getParentId()) : "Document ID mismatch";
    }

    @Test
    public void download() throws Exception {
        final String name = "test.txt";
        String rootId = this.folderManager
                .find(this.token).getObjects().iterator().next().getId();
        Document document =
                manager.create(this.token, Mocks.document(rootId, name));
        String id = document.getId();
        String text = "Hello";
        InputStream upload =
            new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        manager.upload(token, id, upload);
        InputStream download = manager.download(token, id).getStream();
        String readText = new BufferedReader(new InputStreamReader(download))
                .lines()
                .collect(Collectors.joining("\n"));
        assert text.equals(readText) : "Downloaded vs uploaded text mismatch.";
    }

    @Test
    public void upload() throws Exception {
        final String name = "test.txt";
        String rootId = this.folderManager
                .find(this.token).getObjects().iterator().next().getId();
        Document document =
                manager.create(this.token, Mocks.document(rootId, name));
        String id = document.getId();
        InputStream input =
            new ByteArrayInputStream("Hello".getBytes(StandardCharsets.UTF_8));
        manager.upload(token, id, input);
    }

    @Test
    public void retrieve() throws Exception {
        final String name = "test.txt";
        String rootId = this.folderManager
                .find(this.token).getObjects().iterator().next().getId();
        String id = manager
                .create(this.token, Mocks.document(rootId, name)).getId();
        Document document = manager.retrieve(token, id);
        assert id.equals(document.getId()) : "Retrieved document ID mismatch.";
        assert name.equals(document.getName()) :
                "Retrieved document name mismatch.";
        assert rootId.equals(document.getParentId()) :
                "Retrieved document parent ID mismatch.";
    }

    @Test
    public void updateName() throws Exception {
        String name = "test.txt";
        String rootId = this.folderManager
                .find(this.token).getObjects().iterator().next().getId();
        String id = manager
                .create(this.token, Mocks.document(rootId, name)).getId();
        name = "new.txt";
        manager.update(this.token, id, Mocks.document(null, name));
        Document document = manager.retrieve(this.token, id);
        assert id.equals(document.getId()) : "Updated document ID mismatch.";
        assert name.equals(document.getName()) :
                "Updated document name mismatch.";
        assert rootId.equals(document.getParentId()) :
                "Updated document parent ID mismatch.";
    }

    @Test
    public void updateParent() throws Exception {
        String name = "test.txt";
        String rootId = this.folderManager
                .find(this.token).getObjects().iterator().next().getId();
        String id = manager
                .create(this.token, Mocks.document(rootId, name)).getId();
        String folderId = folderManager
                .create(this.token, Mocks.folder(rootId, "test"))
                .getId();
        manager.update(this.token, id, Mocks.document(folderId, null));
        Document document = manager.retrieve(this.token, id);
        assert id.equals(document.getId()) : "Updated document ID mismatch.";
        assert name.equals(document.getName()) :
                "Updated document name mismatch.";
        assert folderId.equals(document.getParentId()) :
                "Updated document parent ID mismatch.";
    }

    @Test
    public void updateNameAndParent() throws Exception {
        String name = "test.txt";
        String rootId = this.folderManager
                .find(this.token).getObjects().iterator().next().getId();
        String id = manager
                .create(this.token, Mocks.document(rootId, name)).getId();
        String folderId = folderManager
                .create(this.token, Mocks.folder(rootId, "test"))
                .getId();
        name = "new.txt";
        manager.update(this.token, id, Mocks.document(folderId, name));
        Document document = manager.retrieve(this.token, id);
        assert id.equals(document.getId()) : "Updated document ID mismatch.";
        assert name.equals(document.getName()) :
                "Updated document name mismatch.";
        assert folderId.equals(document.getParentId()) :
                "Updated document parent ID mismatch.";
    }

    @Test
    public void delete() throws Exception {
        String name = "test.txt";
        String rootId = this.folderManager
                .find(this.token).getObjects().iterator().next().getId();
        String id = manager
                .create(this.token, Mocks.document(rootId, name)).getId();
        manager.delete(this.token, id);
        try {
            manager.retrieve(this.token, id);
            assert false : "NotFoundException should be thrown.";
        } catch (NotFoundException e) {}
    }

    @Test
    public void find() throws Exception {
    }
}