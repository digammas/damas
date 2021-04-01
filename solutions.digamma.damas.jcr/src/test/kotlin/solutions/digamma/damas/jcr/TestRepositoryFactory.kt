package solutions.digamma.damas.jcr

import solutions.digamma.damas.config.Configuration
import solutions.digamma.damas.config.Fallback
import solutions.digamma.damas.logging.Logbook
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.PreDestroy
import javax.annotation.Priority
import javax.decorator.Decorator
import javax.decorator.Delegate
import javax.inject.Inject
import javax.interceptor.Interceptor
import javax.jcr.RepositoryFactory

@Decorator
@Priority(Interceptor.Priority.APPLICATION)
abstract class TestRepositoryFactory: RepositoryFactory {

    @Delegate
    @Inject
    private lateinit var ignore: RepositoryFactory

    @Inject
    private lateinit var log: Logbook

    @Inject
    @Configuration("repository.home")
    @Fallback("storage")
    private lateinit var repositoryHome: String

    @PreDestroy
    fun cleanUp() {
        this.log.info("Deleting files at ${this.repositoryHome}.")
        Files.walk(Paths.get(this.repositoryHome))
            .sorted(Comparator.reverseOrder())
            .map { it.toFile() }
            .forEach { it.delete() }
            this.log.info("Repository home purged.")
    }
}
