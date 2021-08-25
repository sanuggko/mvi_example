package com.sng.patternexample.di

import android.content.Context
import androidx.room.Room
import com.sng.patternexample.room.BlogDao
import com.sng.patternexample.room.BlogDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideBlogDb(@ApplicationContext context: Context): BlogDataBase {
        return Room
            .databaseBuilder(
                context,
                BlogDataBase::class.java,
                BlogDataBase.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBlogDAO(blogDatabase: BlogDataBase): BlogDao {
        return blogDatabase.blogDao()
    }

}