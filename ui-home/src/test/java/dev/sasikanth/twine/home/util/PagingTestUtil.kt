package dev.sasikanth.twine.home.util

import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer

internal suspend fun <T : Any> PagingData<T>.collectDataForTest(): List<T> {
  val differCallback = object : DifferCallback {
    override fun onChanged(position: Int, count: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
  }
  val items = mutableListOf<T>()
  val differ = object : PagingDataDiffer<T>(
    differCallback = differCallback
  ) {
    override suspend fun presentNewList(
      previousList: NullPaddedList<T>,
      newList: NullPaddedList<T>,
      lastAccessedIndex: Int,
      onListPresentable: () -> Unit
    ): Int? {
      for (i in 0 until newList.size) {
        items.add(newList.getFromStorage(i))
      }
      onListPresentable()
      return null
    }
  }
  differ.collectFrom(this)
  return items
}
