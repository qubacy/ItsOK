package com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state

import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.state.BusinessUiState

class ChatUiState(
    var stage: ChatStage = ChatStage.IDLE,
    var messages: List<Message> = listOf(),
    isLoading: Boolean = false,
    error: Error? = null
) : BusinessUiState(isLoading, error) {
    override fun copy(): ChatUiState {
        return ChatUiState(stage, messages.toList(), isLoading, error?.copy())
    }
}