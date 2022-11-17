package dev.sasikanth.twine.home.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.repository.TweetsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagedRecentConversationsUseCase @Inject constructor(
  private val tweetsRepository: TweetsRepository
) : PagedSourceUseCase<RecentConversation> {

  companion object {
    private val PAGING_CONFIG = PagingConfig(
      pageSize = 50,
      initialLoadSize = 50
    )
  }

  override operator fun invoke(): Flow<PagingData<RecentConversation>> {
    return Pager(
      config = PAGING_CONFIG,
      pagingSourceFactory = { tweetsRepository.recentConversations() }
    ).flow
  }
}
