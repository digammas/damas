package solutions.digamma.damas.jcr.repo.job

class NodeTypeDeclaration(
    var types: MutableList<String> = mutableListOf(),
    var name: String? = null,
    var abstract: Boolean = false,
    var mixin: Boolean = false,
    var sortable: Boolean = false,
    var queryable: Boolean? = null,
    var primaryItem: String? = null,
    var properties: MutableList<ChildPropertyDeclaration> = mutableListOf(),
    var nodes: MutableList<ChildNodeDeclaration> = mutableListOf()
) {
    fun types(vararg values: String): NodeTypeDeclaration {
        this.types = values.toMutableList()
        return this
    }

    fun properties(vararg values: ChildPropertyDeclaration):
            NodeTypeDeclaration {
        this.properties = values.toMutableList()
        return this
    }

    fun nodes(vararg values: ChildNodeDeclaration): NodeTypeDeclaration {
        this.nodes = values.toMutableList()
        return this
    }
}

