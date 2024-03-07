package com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state

import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.state.TestUiState

class TestChatUiState(
    stage: ChatStage = ChatStage.IDLE,
    messages: List<Message> = listOf(),
    isLoading: Boolean = false,
    error: Error? = null,
    var getIntroMessagesCalled: Boolean = false,
    var getGripeMessagesCalled: Boolean = false,
    var getMementoMessagesCalled: Boolean = false,
    var moveToByeCalled: Boolean = false,
    var restartCalled: Boolean = false
) : ChatUiState(stage, messages, isLoading, error), TestUiState {

}