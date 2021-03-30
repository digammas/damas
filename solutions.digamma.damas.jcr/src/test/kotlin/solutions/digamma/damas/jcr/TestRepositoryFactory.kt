package solutions.digamma.damas.jcr

import solutions.digamma.damas.config.Configuration
import solutions.digamma.damas.config.Fallback
import java.nio.file.Files
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
    @Configuration("repository.home")
    @Fallback("storage")
    private lateinit var repositoryHome: String

    @PreDestroy
    fun cleanUp() {
        Files.walk(Paths.get(this.repositoryHome)).forEach {
            it.toFile().deleteOnExit()
        }
    }
}
