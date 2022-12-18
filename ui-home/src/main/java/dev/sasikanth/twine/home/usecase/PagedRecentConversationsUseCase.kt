package dev.sasikanth.twine.home.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import dev.sasikanth.twine.data.database.entities.RecentConversation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagedRecentConversationsUseCase @Inject constructor() : PagedSourceUseCase<RecentConversation> {

  companion object {
    private val PAGING_CONFIG = PagingConfig(
      pageSize = 50,
      initialLoadSize = 50
    )
  }

  override operator fun invoke(
    pagingSourceFactory: () -> PagingSource<Int, RecentConversation>,
  ): Flow<PagingData<RecentConversation>> {
    return Pager(
      config = PAGING_CONFIG,
      pagingSourceFactory = pagingSourceFactory
    ).flow
  }
}
