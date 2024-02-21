package com.qubacy.itsok.data.answer.repository.module

import android.content.Context
import com.qubacy.itsok.data.answer.repository.AnswerDataRepository
import com.qubacy.itsok.ui.application.ItsOkApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AnswerDataRepositoryModule {
    @Provides
    fun provideAnswerDataRepository(
        @ApplicationContext context: Context
    ): AnswerDataRepository {
        return AnswerDataRepository((context as ItsOkApplication).db.answerDao())
    }
}