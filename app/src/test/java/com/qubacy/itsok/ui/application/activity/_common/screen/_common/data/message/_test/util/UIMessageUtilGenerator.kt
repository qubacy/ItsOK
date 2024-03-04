package com.qubacy.itsok.ui.application.activity._common.screen._common.data.message._test.util

import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage

object UIMessageUtilGenerator {
    const val DEFAULT_TEXT_PREFIX = "test message"

    fun generateUIMessage(text: String? = DEFAULT_TEXT_PREFIX): UIMessage {
        return UIMessage(text)
    }

    fun generateUIMessages(
        count: Int, textPrefix: String = DEFAULT_TEXT_PREFIX
    ): MutableList<UIMessage> {
        return IntRange(0, count - 1)
            .map { generateUIMessage(textPrefix + it.toString()) }.toMutableList()
    }
}