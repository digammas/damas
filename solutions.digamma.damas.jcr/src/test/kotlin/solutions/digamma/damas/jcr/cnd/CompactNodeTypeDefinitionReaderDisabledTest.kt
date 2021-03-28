package solutions.digamma.damas.jcr.cnd

import org.junit.Before
import org.junit.Test
import solutions.digamma.damas.jcr.repo.cnd.CompactNodeTypeDefinitionReader
import solutions.digamma.damas.logging.Logbook

class CompactNodeTypeDefinitionReaderDisabledTest {

    companion object {
        init {
            System.setProperty("damas.init.skipAll", "true")
        }
    }

    private var importer = CompactNodeTypeDefinitionReader()

    @Before
    fun init() {
        this.importer.logger =
            Logbook(CompactNodeTypeDefinitionReader::class.toString())
        this.importer.skipAllInit = true
        this.importer.init()
    }

    @Test
    fun registerNodeTypes() {
        with (importer.produceJob()) {
            assert(namespaces.isEmpty() && types.isEmpty() && nodes.isEmpty()) {
                "Expecting empty job"
            }
        }
    }
}
