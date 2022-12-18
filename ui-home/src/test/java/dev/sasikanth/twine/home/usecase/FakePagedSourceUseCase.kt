package dev.sasikanth.twine.home.usecase

import androidx.paging.PagingData
import androidx.paging.PagingSource
import dev.sasikanth.twine.data.database.entities.RecentConversation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// TODO: Remove and use the paged source use case directly with fake repository
class FakePagedSourceUseCase : PagedSourceUseCase<RecentConversation> {

  private val _pagingData = MutableStateFlow(PagingData.empty<RecentConversation>())

  override fun invoke(pagingSourceFactory: () -> PagingSource<Int, RecentConversation>): Flow<PagingData<RecentConversation>> {
    return _pagingData
  }

  fun addPagingData(pagingData: PagingData<RecentConversation>) {
    _pagingData.value = pagingData
  }

  fun clear() {
    _pagingData.value = PagingData.empty()
  }
}
