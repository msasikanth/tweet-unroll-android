package dev.sasikanth.twine.home.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface PagedSourceUseCase<T : Any> {

  operator fun invoke(): Flow<PagingData<T>>
}
