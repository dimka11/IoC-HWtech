package HWdTech.IoC

class ScopeImpl(closingStrategy_: () -> Unit) : IScope {
    private val strategies = hashMapOf<Any, IIoCResolverStrategy>()
    private val closingStrategy = closingStrategy_

    override fun register(key: Any, strategy: IIoCResolverStrategy) {
        strategies.put(key, strategy)
    }

    override fun resolve(key: Any): IIoCResolverStrategy {
        return checkNotNull(strategies.get(key))
    }

    override fun close() {
        closingStrategy()
    }
}
