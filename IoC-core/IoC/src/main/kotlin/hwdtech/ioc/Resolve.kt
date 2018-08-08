package hwdtech.ioc

inline fun <reified T> resolve(key: Any, vararg args: Any): T {
    try {
        @Suppress("UNCHECKED_CAST")
        return Scopes.current.resolve(key)(args) as T
    } catch (ex: ClassCastException) {
        throw ResolveDependencyError("Could not cast object of dependency '$key' to type ${T::class.qualifiedName}")
    }
}
