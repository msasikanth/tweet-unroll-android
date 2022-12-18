package dev.sasikanth.twine.common.testing.dispatchers

import dev.sasikanth.twine.common.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestDispatchers : CoroutineDispatchers {

  override val io = UnconfinedTestDispatcher()

  override val main = UnconfinedTestDispatcher()

  override val default = UnconfinedTestDispatcher()
}
