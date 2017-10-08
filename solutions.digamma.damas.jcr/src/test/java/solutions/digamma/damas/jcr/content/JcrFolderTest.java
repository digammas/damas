package solutions.digamma.damas.jcr.content;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.jcr.Mocks;
import solutions.digamma.damas.jcr.WeldTest;

import java.util.List;

/**
 * Test folder object.
 *
 * Created by Ahmad on 9/3/17.
 */
public class JcrFolderTest extends WeldTest {

    private Token token;
    private Folder folder;

    @Before
    public void setUp() throws Exception {
        FolderManager manager = inject(JcrFolderManager.class);
        this.token = this.login.login("admin", "admin");
        String parentId = manager
                .find(this.token).getObjects().iterator().next().getId();
        Folder parent = this.folder = manager.create(this.token,
                Mocks.folder(parentId, "test"));
        for (int i = 0; i < 10; i++) {
            parentId = parent.getId();
            String name = String.format("test_%d", i);
            parent = manager.create(this.token,
                    Mocks.folder(parentId, name));
        }
    }

    @After
    public void tearDown() throws Exception {
        this.login.logout(this.token);
    }

    @Test
    public void expand() throws Exception {
        this.folder.expand();

    }

    @Test
    public void expandContent() throws Exception {
        Folder expanded = this.folder;
        expanded.expandContent();
        for (int i = 0; i < 10; i++) {
            expanded = expanded.getContent().getFolders().iterator().next();
        }
    }

    @Test
    public void expandContentToDepth() throws Exception {
        Folder expanded = this.folder;
        int depth = 5;
        expanded.expandContent(depth);
        for (int i = 0; i < depth; i++) {
            expanded = expanded.getContent().getFolders().iterator().next();
        }
        assert expanded.getContent() == null;
    }

    @Test
    public void getContent() throws Exception {
        this.folder.expandContent();
        List<? extends Folder> folders = this.folder.getContent().getFolders();
        assert folders.size() != 0;
        Folder subfolder = folders.iterator().next();
        assert subfolder.getName().equals(String.format("test_%d", 0));
    }
}