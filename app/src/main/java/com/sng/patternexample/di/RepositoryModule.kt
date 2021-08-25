package com.sng.patternexample.di

import com.sng.patternexample.repository.BlogRepository
import com.sng.patternexample.retrofit.BlogRetrofit
import com.sng.patternexample.retrofit.NetworkMapper
import com.sng.patternexample.room.BlogDao
import com.sng.patternexample.room.CacheMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Singleton
    @Provides
    fun provideBlogRepository(
        blogDao: BlogDao,
        retrofit: BlogRetrofit,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ): BlogRepository {
        return BlogRepository(retrofit, blogDao, cacheMapper, networkMapper)
    }
}