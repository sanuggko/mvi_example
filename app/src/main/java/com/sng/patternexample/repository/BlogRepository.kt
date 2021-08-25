package com.sng.patternexample.repository

import com.sng.patternexample.model.Blog
import com.sng.patternexample.retrofit.BlogRetrofit
import com.sng.patternexample.retrofit.NetworkMapper
import com.sng.patternexample.room.BlogDao
import com.sng.patternexample.room.CacheMapper
import com.sng.patternexample.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BlogRepository @Inject constructor(
    private val retrofit: BlogRetrofit,
    private val blogDao: BlogDao,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
) {

    suspend fun getBlogs(): Flow<DataState<List<Blog>>> = flow {
        emit(DataState.Loading)
        try {
            val networkBlogs = retrofit.getBlogs()
            val blogs = networkMapper.mapToEntityList(networkBlogs)

            for (blog in blogs) {
                blogDao.insert(cacheMapper.mapToEntity(blog))
            }

            val cachedBlogs = blogDao.get()
            emit(DataState.Success(cacheMapper.mapFromEntityList(cachedBlogs)))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

}