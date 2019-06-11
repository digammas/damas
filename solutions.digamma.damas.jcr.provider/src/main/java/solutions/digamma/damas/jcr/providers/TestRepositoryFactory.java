package solutions.digamma.damas.jcr.providers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.jcr.RepositoryFactory;

/**
 * Repository factory decorator that drops repository upon shutdown.
 */
@Decorator
@Priority(Interceptor.Priority.APPLICATION)
class TestRepositoryFactory extends ModeShapeRepositoryFactory {

    @Delegate
    @Inject
    private RepositoryFactory ignore;

    @Inject
    private Logger logger;

    @PreDestroy
    public void cleanUp() throws IOException {
        if (!Files.walk(Paths.get("repository"))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .map(File::delete)
                .reduce(Boolean::logicalAnd)
                .orElse(true)) {
            this.logger.log(Level.SEVERE, "Test repository could not be deleted.");
        }
    }
}
