package hwdtech.ioc

import java.io.Closeable

interface IScope : Closeable {
    fun resolve(key: Any): IIoCResolverStrategy
    fun register(key: Any, strategy: IIoCResolverStrategy)
}
