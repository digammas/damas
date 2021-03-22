package solutions.digamma.damas.jcr.repo.job

class ChildNodeDeclaration(
    var name: String? = null,
    var types: MutableList<String> = mutableListOf(),
    var defaultType: String? = null,
    var primary: Boolean = false,
    var mandatory: Boolean = false,
    var autocreated: Boolean = false,
    var protected: Boolean = false,
    var multiple: Boolean = false,
    var version: Int = 1,
) {

    fun types(vararg values: String): ChildNodeDeclaration {
        this.types = values.toMutableList()
        return this
    }
}
