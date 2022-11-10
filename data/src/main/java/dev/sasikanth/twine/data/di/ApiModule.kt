package dev.sasikanth.twine.data.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.common.utils.Constants.BASE_API_URL
import dev.sasikanth.twine.data.api.TwitterApi
import dev.sasikanth.twine.data.util.InstantMoshiAdapter
import dev.sasikanth.twine.data.util.LocalDateTimeMoshiAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

  @Provides
  @Singleton
  fun providesMoshi(): Moshi {
    return Moshi.Builder()
      .add(LocalDateTimeMoshiAdapter())
      .add(InstantMoshiAdapter())
      .build()
  }

  @Provides
  @Singleton
  fun providesRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(BASE_API_URL)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .addConverterFactory(ScalarsConverterFactory.create())
      .build()
  }

  @Provides
  @Singleton
  fun providesTwitterApi(retrofit: Retrofit): TwitterApi {
    return retrofit.create()
  }
}
