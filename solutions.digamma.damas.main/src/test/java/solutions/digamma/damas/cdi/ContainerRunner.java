package solutions.digamma.damas.cdi;

import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.List;

/**
 * Weld test runner.
 *
 * @author Ahmad Shahwan
 */
public class ContainerRunner extends BlockJUnit4ClassRunner {

    private final ContainerRule resource = new ContainerRule();

    /**
     * Constructor.
     *
     * @param klass
     * @throws InitializationError
     */
    public ContainerRunner(Class<?> klass) throws InitializationError {
        super(klass);

    }

    @Override
    public Object createTest() {
        return this.resource.get(this.getTestClass().getJavaClass());
    }

    @Override
    protected List<TestRule> classRules() {
        List<TestRule> rules = super.classRules();
        rules.add(resource);
        return rules;
    }
}
