package hwdtech.ioc

fun register(key: Any, strategy: IIoCResolverStrategy) {
    Scopes.current.register(key, strategy)
}
