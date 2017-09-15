package solutions.digamma.damas.jcr.content;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.jcr.Mocks;
import solutions.digamma.damas.jcr.WeldTest;

/**
 * Created by ahmad on 9/3/17.
 */
public class JcrFolderTest extends WeldTest {

    private FolderManager manager;
    private Token token;
    private Folder folder;

    @Before
    public void setUp() throws Exception {
        this.manager = inject(JcrFolderManager.class);
        this.token = this.login.login("admin", "admin");
        String parentId = this.manager
                .find(this.token).getObjects().iterator().next().getId();
        Folder parent = this.folder = manager.create(this.token,
                Mocks.folder(parentId, "test"));
        for (int i = 0; i < 10; i++) {
            parentId = parent.getId();
            parent = this.folder = manager.create(this.token,
                    Mocks.folder(parentId, "test"));
        }

    }

    @After
    public void tearDown() throws Exception {
        this.login.logout(this.token);
    }

    @Test
    public void expand() throws Exception {
    }

    @Test
    public void expandContent() throws Exception {
    }

    @Test
    public void expandContent1() throws Exception {
    }

    @Test
    public void getContent() throws Exception {
    }

}