package hwdtech.ioc

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
        Scopes.startNew().use { s ->
            val parentScope = s
            assertSame(s, Scopes.current, "Created scope should be a current scope.")

            Scopes.startNew().use {
                assertNotSame(parentScope, it, "Created scope should be different from previous")
                assertSame(it, Scopes.current, "Created new scope should be a current scope.")
            }

            assertSame(s, Scopes.current, "Current scope should be equal to parent scope")
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
        Scopes.startNew().use {
            var wasCalled = false
            it.register("dep", { wasCalled = true })

            it.resolve("dep")(arrayOf())

            assertTrue(wasCalled)
        }
    }

    @test
    fun `Scopes should use parent scope to resolve dependency`() {
        Scopes.startNew().use { s ->
            val rootScope = s
            var wasCalled = false

            s.register("dep", { wasCalled = true })

            Scopes.startNew().use {

                Scopes.startNew().use {
                    assertNotSame(rootScope, it, "Current scope must be not same as initial scope.")

                    it.resolve("dep")(arrayOf())

                    assertTrue(wasCalled)
                }
            }
        }
    }

    @test(expected = ResolveDependencyError::class)
    fun `Root scope should throw ResolveSependencyError if could not resolve dependency by key`() {
        Scopes.startNew().use {
            it.resolve("dep")
        }
    }

    @test(expected = ResolveDependencyError::class)
    fun `Child scope should throw ResolveSependencyError if could not resolve dependency by key`() {
        Scopes.startNew().use {
            val rootScope = it

            Scopes.startNew().use {
                assertNotSame(rootScope, it, "Current scope must be not same as initial scope.")

                it.resolve("dep")
            }
        }
    }

    @test
    fun `Child scope should allow to replace IIoCResolverStartegy`() {
        Scopes.startNew().use {
            Scopes.startNew().use {
                var wasCalled1 = false
                var wasCalled2 = false


                it.register("dep", { wasCalled1 = true })
                it.register("dep", { wasCalled2 = true })

                it.resolve("dep")(arrayOf())

                assertTrue(!wasCalled1 && wasCalled2)
            }
        }
    }

    @test
    fun `Root scope should allow to replace IIoCResolverStartegy`() {
        Scopes.startNew().use {
            var wasCalled1 = false
            var wasCalled2 = false

            it.register("dep", { wasCalled1 = true })
            it.register("dep", { wasCalled2 = true })

            it.resolve("dep")(arrayOf())

            assertTrue(!wasCalled1 && wasCalled2)
        }
    }

    @test
    fun `Child scope should overlap dependency`() {
        var wasCalled1 = false
        var wasCalled2 = false

        Scopes.startNew().use {
            it.register("dep", { wasCalled1 = true })

            Scopes.startNew().use {
                it.register("dep", { wasCalled2 = true })

                it.resolve("dep")(arrayOf())

                assertTrue(!wasCalled1 && wasCalled2)
            }
            it.resolve("dep")(arrayOf())

            assertTrue(wasCalled1)
        }
    }
}
