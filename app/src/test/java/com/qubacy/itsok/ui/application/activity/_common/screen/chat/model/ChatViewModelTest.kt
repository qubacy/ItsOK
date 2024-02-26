package com.qubacy.itsok.ui.application.activity._common.screen.chat.model

import androidx.lifecycle.SavedStateHandle
import com.qubacy.itsok.domain.chat.ChatUseCase
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModelTest
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState

class ChatViewModelTest : BaseViewModelTest<ChatUiState, ChatUseCase, ChatViewModel>(
    ChatUseCase::class.java
) {
    override fun createViewModel(
        savedStateHandle: SavedStateHandle,
        useCase: ChatUseCase
    ): ChatViewModel {
        return ChatViewModel(savedStateHandle, useCase)
    }


}