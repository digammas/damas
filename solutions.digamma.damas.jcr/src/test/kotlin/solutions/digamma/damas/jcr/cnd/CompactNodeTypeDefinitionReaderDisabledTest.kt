package solutions.digamma.damas.jcr.cnd

import org.junit.Test
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.jcr.repo.cnd.CompactNodeTypeDefinitionReader

class CompactNodeTypeDefinitionReaderDisabledTest  : WeldTest() {

    companion object {
        init {
            System.setProperty("damas.init.skipAll", "true")
        }
    }

    private var importer = WeldTest
        .inject(CompactNodeTypeDefinitionReader::class.java)

    @Test
    fun registerNodeTypes() {
        with (importer.produceJob()) {
            assert(namespaces.isEmpty() && types.isEmpty() && nodes.isEmpty()) {
                "Expecting empty job"
            }
        }
    }
}
