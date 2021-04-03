package solutions.digamma.damas.cdi;

import org.junit.ClassRule;

public class ContainerTest {

    @ClassRule
    public static ContainerRule containerRule = new ContainerRule();

    public static <T> T inject(Class<T> klass) {
        return containerRule.get(klass);
    }
}
