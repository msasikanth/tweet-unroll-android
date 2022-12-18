package dev.sasikanth.twine.home.usecase

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

interface PagedSourceUseCase<T : Any> {

  operator fun invoke(pagingSourceFactory: () -> PagingSource<Int, T>): Flow<PagingData<T>>
}
