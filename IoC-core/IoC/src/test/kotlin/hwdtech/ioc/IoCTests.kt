package hwdtech.ioc

import org.mockito.Mockito
import org.mockito.Mockito.mock
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.junit.Test as test

class `IoC tests` {
    @test
    fun `register and resolve methods should be used to resolve dependency`() {
        Scopes.startNew().use {
            var wasCalled = false
            val obj = 1

            register("dep", { wasCalled = true; return@register obj; })
            assertSame(obj, resolve("dep"))
            assertTrue(wasCalled)
        }
    }

    @test(expected = ResolveDependencyError::class)
    fun `resolve method should throw ResolveDependencyError if could not resolve dependency`() {
        Scopes.startNew().use {
            resolve<Any>("dep")
        }
    }

    @test(expected = ResolveDependencyError::class)
    fun `resolve method should throw ResolveDependencyError if could not cast object`() {
        Scopes.startNew().use {
            val obj: Any = 1

            register("dep", { return@register obj; })
            resolve<String>("dep")
        }
    }

    @test
    fun `resolve should cast object to required type`() {
        Scopes.startNew().use {
            val obj: Any = 1

            register("dep", { return@register obj; })
            assertEquals(1, resolve<Int>("dep"))
        }
    }

    @test
    fun `resolve method should use Scopes`() {
        Scopes.startNew().use {
            val m = mock(IScope::class.java)

            Mockito.`when`(m.resolve("dep")).thenReturn { to_: Array<out Any> -> Any() }

            Scopes.current = m
            resolve<Any>("dep")

            Mockito.verify(m).resolve("dep")
        }
    }

    @test
    fun `register method should use Scopes`() {
        Scopes.startNew().use {
            val m = mock(IScope::class.java)

            val resolverStartegy = { to_: Array<out Any> -> Any() }

            Mockito.doNothing().`when`(m).register("dep", resolverStartegy)

            Scopes.current = m
            register("dep", resolverStartegy)

            Mockito.verify(m).register("dep", resolverStartegy)
        }
    }
}
