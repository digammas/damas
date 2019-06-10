package solutions.digamma.damas.jcr.providers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.jcr.RepositoryFactory;

@Decorator
@Priority(Interceptor.Priority.APPLICATION)
class TestRepositoryFactory extends ModeShapeRepositoryFactory {

    @Delegate
    @Inject
    private RepositoryFactory delegate;

    @PreDestroy
    public boolean cleanUp() throws IOException {
        return Files.walk(Paths.get("repository"))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .map(File::delete)
                .reduce(Boolean::logicalAnd)
                .orElse(true);
    }
}
