package HWdTech.IoC

import java.util.concurrent.CyclicBarrier
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.junit.Test as test

class `Scopes tests` {
    @test
    fun `Current scope always exists`() {
        Scopes.current
    }

    @test
    fun `Double call of current should return same scope`() {
        assertSame(Scopes.current, Scopes.current)
    }


    @test
    fun `New scope created by startNew must be the current scope`() {
        Scopes.startNew().use {
            assertSame(it, Scopes.current, "Created scope should be a current scope.")
        }
    }

    @test
    fun `use should return back to previous scope`() {
        Scopes.startNew().use {
            val parentScope = it
            assertSame(it, Scopes.current, "Created scope should be a current scope.")

            Scopes.startNew().use {
                assertNotSame(parentScope, it, "Created scope should be different from previous")
                assertSame(it, Scopes.current, "Created new scope should be a current scope.")
            }

            assertSame(it, Scopes.current, "Current scope should be equal to parent scope")
        }
    }

    @test
    fun `Current scope always exists even if single scope was deleted before`() {
        Scopes.current.use {

        }
        Scopes.current
    }

    @test
    fun `Scopes in different threads are diffrent`() {
        var scope1 = Scopes.current
        var scope2 = Scopes.current

        val barrier = CyclicBarrier(3)

        val thread1 = Thread(Runnable {
            scope1 = Scopes.current
            barrier.await()
        })

        val thread2 = Thread(Runnable {
            scope2 = Scopes.current
            barrier.await()
        })

        thread1.start()
        thread2.start()

        barrier.await()

        assertNotSame(scope1, scope2)
    }

    @test
    fun `Scopes should register and resolve dependency`() {
        var wasCalled = false

        Scopes.startNew().use {
            it.register("dep", { wasCalled = true })
            it.resolve("dep")(arrayOf())
        }

        assertTrue(wasCalled)
    }

    @test
    fun `Scopes should use parent scope to resolve dependency`() {
        var wasCalled = false

        Scopes.startNew().use {
            it.register("dep", { wasCalled = true })

            Scopes.startNew().use {

                Scopes.startNew().use {
                    it.resolve("dep")(arrayOf())
                }
            }
        }

        assertTrue(wasCalled)
    }

}
