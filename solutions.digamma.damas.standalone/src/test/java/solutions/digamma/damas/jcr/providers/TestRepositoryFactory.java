package solutions.digamma.damas.jcr.providers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
abstract class TestRepositoryFactory implements RepositoryFactory {

    @Delegate
    @Inject
    private RepositoryFactory ignore;

    @PreDestroy
    public void cleanUp() throws IOException {
        Files.walk(Paths.get("repository"))
                .map(Path::toFile)
                .forEach(File::deleteOnExit);
    }
}
