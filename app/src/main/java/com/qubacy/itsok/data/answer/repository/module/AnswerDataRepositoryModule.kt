package com.qubacy.itsok.data.answer.repository.module

import com.qubacy.itsok.data.answer.repository.AnswerDataRepository
import com.qubacy.itsok.data.answer.repository.source.local.LocalAnswerDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AnswerDataRepositoryModule {
    @Provides
    fun provideAnswerDataRepository(
        localAnswerDataSource: LocalAnswerDataSource
    ): AnswerDataRepository {
        return AnswerDataRepository(localAnswerDataSource)
    }
}