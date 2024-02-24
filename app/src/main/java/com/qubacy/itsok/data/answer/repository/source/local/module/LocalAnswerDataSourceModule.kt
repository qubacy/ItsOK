package com.qubacy.itsok.data.answer.repository.source.local.module

import android.content.Context
import com.qubacy.itsok.data.answer.repository.source.local.LocalAnswerDataSource
import com.qubacy.itsok.ui.application.ItsOkApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalAnswerDataSourceModule {
    @Provides
    fun provideLocalAnswerDataSource(
        @ApplicationContext context: Context
    ): LocalAnswerDataSource {
        return (context as ItsOkApplication).db.answerDao()
    }
}