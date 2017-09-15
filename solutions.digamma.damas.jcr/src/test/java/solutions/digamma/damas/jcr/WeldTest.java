package solutions.digamma.damas.jcr;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import solutions.digamma.damas.auth.LoginManager;

/**
 * @author Ahmad Shahwan
 */
public class WeldTest {

    protected static WeldContainer container;

    protected LoginManager login;

    @Before
    public void setUpWeld() throws Exception {
        this.login = inject(LoginManager.class);
    }

    @After
    public void tearDownWeld() throws Exception {
    }

    @BeforeClass
    public static void setUpClass() {
        container = new Weld().initialize();
    }

    @AfterClass
    public static void tearDownClass() {
        container.close();
    }

    protected static <T> T inject(Class<T> kass) {
        return container.select(kass).get();
    }

}
