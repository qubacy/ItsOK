package com.qubacy.itsok.ui.application.activity._common.screen.chat.model.operation

import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.operation._common.UiOperation

class NextMessagesUiOperation(
    val messages: List<Message>
) : UiOperation {

}