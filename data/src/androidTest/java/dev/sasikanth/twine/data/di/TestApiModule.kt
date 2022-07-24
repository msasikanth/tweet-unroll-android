package dev.sasikanth.twine.data.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.common.moshi.LocalDateTimeMoshiAdapter
import dev.sasikanth.twine.data.BuildConfig
import dev.sasikanth.twine.common.utils.Constants.BASE_API_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestApiModule {

  @Provides
  @Singleton
  fun providesTestOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor { chain ->
        val request = chain.request()
          .newBuilder()
          .addHeader(
            "Authorization",
            "Bearer ${BuildConfig.TEST_TWINE_BEARER_TOKEN}"
          )
          .build()

        chain.proceed(request)
      }
      .build()
  }

  @Provides
  @Singleton
  fun providesMoshi(): Moshi {
    return Moshi.Builder()
      .add(LocalDateTimeMoshiAdapter())
      .build()
  }

  @Provides
  @Singleton
  fun providesTestRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi
  ): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(BASE_API_URL)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .addConverterFactory(ScalarsConverterFactory.create())
      .build()
  }
}
