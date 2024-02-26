package com.qubacy.itsok.domain.chat.model._test.util

import com.qubacy.itsok.domain.chat.model.Message

object MessageGeneratorUtil {
    const val DEFAULT_TEXT_PREFIX = "test message"

    fun generateMessage(text: String? = DEFAULT_TEXT_PREFIX): Message {
        return Message(text)
    }

    fun generateMessages(
        count: Int, textPrefix: String = DEFAULT_TEXT_PREFIX
    ): List<Message> {
        return IntRange(0, count - 1)
            .map { generateMessage(textPrefix + it.toString()) }
    }
}