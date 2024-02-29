package com.qubacy.itsok.domain.chat.usecase.module

import com.qubacy.itsok.data.answer.repository.AnswerDataRepository
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.domain.chat.usecase.ChatUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ChatUseCaseModule {
    @Provides
    fun provideChatUseCase(
        errorDataRepository: ErrorDataRepository,
        answerDataRepository: AnswerDataRepository,
        mementoDataRepository: MementoDataRepository
    ): ChatUseCase {
        return ChatUseCase(errorDataRepository, answerDataRepository, mementoDataRepository)
    }
}