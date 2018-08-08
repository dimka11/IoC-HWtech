package HWdTech.IoC

import kotlin.test.assertNotSame
import kotlin.test.assertSame
import org.junit.Test as test

class `Scopes tests` {
    @test
    fun `Current scope always exists`() {
        Scopes.current
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
}
