package com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state

import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState

class ChatUiState(
    var stage: ChatStage = ChatStage.IDLE,
    var messages: List<Message> = listOf(),
    error: Error?
) : BaseUiState(error) {
    override fun copy(): ChatUiState {
        return ChatUiState(stage, messages.toList(), error?.copy())
    }
}