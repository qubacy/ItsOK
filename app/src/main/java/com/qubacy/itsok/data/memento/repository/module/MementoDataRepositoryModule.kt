package com.qubacy.itsok.data.memento.repository.module

import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.data.memento.repository.source.local.LocalMementoDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MementoDataRepositoryModule {
    @Provides
    fun provideMementoDataRepository(
        localMementoDataSource: LocalMementoDataSource
    ): MementoDataRepository {
        return MementoDataRepository(localMementoDataSource)
    }
}