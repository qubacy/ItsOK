package com.qubacy.itsok.data.memento.repository.source.local.module

import android.content.Context
import com.qubacy.itsok.data.memento.repository.source.local.LocalMementoDataSource
import com.qubacy.itsok.ui.application.ItsOkApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalMementoDataSourceModule {
    @Provides
    fun provideLocalMementoDataSource(
        @ApplicationContext context: Context
    ): LocalMementoDataSource {
        return (context as ItsOkApplication).db.mementoDao()
    }
}