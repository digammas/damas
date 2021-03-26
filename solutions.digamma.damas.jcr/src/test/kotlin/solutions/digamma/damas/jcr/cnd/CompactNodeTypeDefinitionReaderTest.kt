package solutions.digamma.damas.jcr.cnd

import org.junit.Test
import solutions.digamma.damas.jcr.WeldTest
import solutions.digamma.damas.jcr.repo.cnd.CompactNodeTypeDefinitionReader

class CompactNodeTypeDefinitionReaderTest  : WeldTest() {

    private var importer = WeldTest
        .inject(CompactNodeTypeDefinitionReader::class.java)

    @Test
    fun registerNodeTypes() {
        with (this.importer.produceJob()) {
            assert(namespaces.isNotEmpty()) { "Expecting namesapces" }
            assert(types.isNotEmpty()) { "Expecting types" }
            assert(nodes.isEmpty()) { "Unexpected creation" }
        }
    }
}
