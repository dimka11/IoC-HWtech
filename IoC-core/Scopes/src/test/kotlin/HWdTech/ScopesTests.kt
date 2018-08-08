package HWdTech.IoC

import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import org.junit.Test as test

class `Scopes tests` {
    @test
    fun `Current scope is not null always`() {
        assertNotNull(Scopes.current, "Current scope should be not null.")
    }

    @test
    fun `New scope created by startNew must be a not null`() {
        Scopes.startNew().use {
            assertNotNull(it, "Created scope should be not null.")
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
}
