package com.qubacy.itsok.data.error.repository.module

import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.data.error.repository.source.local.LocalErrorDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ErrorDataRepositoryModule {
    @Provides
    fun provideErrorDataRepository(
        localErrorDataSource: LocalErrorDataSource
    ): ErrorDataRepository {
        return ErrorDataRepository(localErrorDataSource)
    }
}