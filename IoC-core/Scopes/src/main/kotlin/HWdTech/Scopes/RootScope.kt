package HWdTech.IoC

class RootScope : IScope {
    private val strategies = hashMapOf<Any, IIoCResolverStrategy>()

    override fun register(key: Any, strategy: IIoCResolverStrategy) {
        strategies.put(key, strategy)
    }

    override fun resolve(key: Any): IIoCResolverStrategy {
        return strategies.getOrElse(key, { throw ResolveDependencyError("Dependency $key") })
    }

    override fun close() {
        Scopes.remove()
    }
}
