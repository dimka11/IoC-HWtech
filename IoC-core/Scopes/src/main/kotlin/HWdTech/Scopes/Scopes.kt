package HWdTech.IoC

import kotlin.concurrent.getOrSet

open class Scopes {
    companion object {
        private val scopesInvoker: ThreadLocal<IScope> = ThreadLocal<IScope>()

        var current: IScope
            get() {
                return scopesInvoker.getOrSet {
                    newScope()
                }
            }
            set(value) {
                scopesInvoker.set(value)
            }

        fun startNew(): IScope {
            var nextScope = { s: IScope -> newScope(s) }

            val currentScope = scopesInvoker.getOrSet({
                nextScope = { s: IScope -> s }
                return@getOrSet newScope()
            })

            current = nextScope(currentScope)

            return current
        }

        private fun newScope(parentScope: IScope): IScope {
            return ChildScope(parentScope)
        }

        private fun newScope(): IScope {
            return RootScope()
        }

        internal fun remove() {
            scopesInvoker.remove()
        }
    }
}


