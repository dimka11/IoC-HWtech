package HWdTech.IoC

interface IScope {
    fun resolve(key: Any): IIoCResolverStrategy
    fun register(key: Any, strategy: IIoCResolverStrategy)
}
