package solutions.digamma.damas.jcr.repo.job

class ChildPropertyDeclaration(
    var name: String? = null,
    var type: Int? = null,
    var defaultValue: Any? = null,
    var defaultValues: MutableList<Any?> = mutableListOf(),
    var primary: Boolean = false,
    var constraints: MutableList<String> = mutableListOf(),
    var mandatory: Boolean = false,
    var autocreated: Boolean = false,
    var protected: Boolean = false,
    var multiple: Boolean = false,
    var searchable: Boolean = true,
    var queryOrder: Boolean = true,
    var queryOperators: MutableList<String>? = null,
    var version: Int = 1,
) {

    init {
        if (defaultValue != null && defaultValues.isNotEmpty()) {
            defaultValues = mutableListOf(defaultValue)
        }
    }

    fun constraints(vararg values: String): ChildPropertyDeclaration {
        this.constraints = values.toMutableList()
        return this
    }
}
