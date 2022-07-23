package dev.sasikanth.twine.common.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface CoroutineDispatchers {
  val default: CoroutineDispatcher
  val io: CoroutineDispatcher
  val main: CoroutineDispatcher
}

class RealCoroutineDispatchers @Inject constructor() : CoroutineDispatchers {

  override val default: CoroutineDispatcher
    get() = Dispatchers.Default

  override val io: CoroutineDispatcher
    get() = Dispatchers.IO

  override val main: CoroutineDispatcher
    get() = Dispatchers.Main
}
