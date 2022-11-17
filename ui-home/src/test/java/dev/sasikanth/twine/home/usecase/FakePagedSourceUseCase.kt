package dev.sasikanth.twine.home.usecase

import androidx.paging.PagingData
import dev.sasikanth.twine.data.database.entities.RecentConversation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePagedSourceUseCase : PagedSourceUseCase<RecentConversation> {

  private val _pagingData = MutableStateFlow(PagingData.empty<RecentConversation>())

  override fun invoke(): Flow<PagingData<RecentConversation>> {
    return _pagingData
  }

  fun addPagingData(pagingData: PagingData<RecentConversation>) {
    _pagingData.value = pagingData
  }

  fun clear() {
    _pagingData.value = PagingData.empty()
  }
}
