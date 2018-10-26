package hwdtech.ioc

class ChildScope(parent: IScope) : IScope {
    private val strategies = hashMapOf<Any, IIoCResolverStrategy>()
    private val parentScope = parent

    override fun register(key: Any, strategy: IIoCResolverStrategy) {
        strategies.put(key, strategy)
    }

    override fun resolve(key: Any): IIoCResolverStrategy {
        return strategies.getOrElse(key, { return@getOrElse parentScope.resolve(key) })
    }

    override fun close() {
        Scopes.current = parentScope
    }
}
