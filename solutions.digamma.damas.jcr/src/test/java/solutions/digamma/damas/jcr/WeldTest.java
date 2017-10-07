package solutions.digamma.damas.jcr;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import solutions.digamma.damas.auth.LoginManager;

/**
 * @author Ahmad Shahwan
 */
public class WeldTest {

    @ClassRule
    public static WeldRule weld = new WeldRule();

    protected LoginManager login;

    @Before
    public void setUpWeld() throws Exception {
        this.login = inject(LoginManager.class);
    }

    @After
    public void tearDownWeld() throws Exception {
    }

    protected static <T> T inject(Class<T> klass) {
        return weld.inject(klass);
    }

}
