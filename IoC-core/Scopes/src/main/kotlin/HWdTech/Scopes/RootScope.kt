package HWdTech.IoC

class RootScope : IScope {
    private val strategies = hashMapOf<Any, IIoCResolverStrategy>()

    override fun register(key: Any, strategy: IIoCResolverStrategy) {
        strategies.put(key, strategy)
    }

    override fun resolve(key: Any): IIoCResolverStrategy {
        return checkNotNull(strategies.get(key))
    }

    override fun close() {
        Scopes.remove()
    }
}
