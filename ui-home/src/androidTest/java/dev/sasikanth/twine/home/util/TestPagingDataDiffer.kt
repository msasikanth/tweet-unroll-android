package dev.sasikanth.twine.home.util

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlin.coroutines.CoroutineContext

internal class TestPagingDataDiffer<T : Any>(
  diffCallback: DiffUtil.ItemCallback<T>,
  testDispatcher: CoroutineContext = StandardTestDispatcher()
) {
  private val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
    }

    override fun onRemoved(position: Int, count: Int) {
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
    }
  }

  private val differ = AsyncPagingDataDiffer<T>(
    diffCallback = diffCallback,
    updateCallback = noopListUpdateCallback,
    mainDispatcher = testDispatcher,
    workerDispatcher = testDispatcher
  )

  suspend fun submitData(data: PagingData<T>) {
    differ.submitData(data)
  }

  fun assertNoContent() {
    assertThat(differ.snapshot().items).isEmpty()
  }

  fun assertContent(vararg expected: T) {
    assertThat(differ.snapshot().items).containsExactly(*expected)
  }
}
