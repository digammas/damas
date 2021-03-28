package solutions.digamma.damas.jcr.cnd

import org.junit.Before
import org.junit.Test
import solutions.digamma.damas.jcr.repo.cnd.CompactNodeTypeDefinitionReader
import solutions.digamma.damas.logging.Logbook

class CompactNodeTypeDefinitionReaderTest {

    private var importer = CompactNodeTypeDefinitionReader()

    @Before
    fun init() {
        this.importer.logger =
            Logbook(CompactNodeTypeDefinitionReader::class.toString())
        this.importer.init()
    }

    @Test
    fun registerNodeTypes() {
        with (this.importer.produceJob()) {
            assert(namespaces.isNotEmpty()) { "Expecting namesapces" }
            assert(types.isNotEmpty()) { "Expecting types" }
            assert(nodes.isEmpty()) { "Unexpected creation" }
        }
    }
}
