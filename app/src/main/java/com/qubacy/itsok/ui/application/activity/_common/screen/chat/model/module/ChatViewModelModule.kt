package com.qubacy.itsok.ui.application.activity._common.screen.chat.model.module

import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain.chat.usecase.ChatUseCase
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModelFactory
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModelFactoryQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object ChatViewModelModule {
    @Provides
    @ChatViewModelFactoryQualifier
    fun provideChatViewModelFactory(
        errorDataRepository: ErrorDataRepository,
        chatUseCase: ChatUseCase
    ): ViewModelProvider.Factory {
        return ChatViewModelFactory(errorDataRepository, chatUseCase)
    }
}