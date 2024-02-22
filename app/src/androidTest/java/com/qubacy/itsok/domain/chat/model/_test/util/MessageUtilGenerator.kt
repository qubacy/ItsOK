package com.qubacy.itsok.domain.chat.model._test.util

import com.qubacy.itsok.domain.chat.model.Message

object MessageUtilGenerator {
    const val DEFAULT_PREFIX = "test message "

    fun generateMessage(posIndex: Int = 0): Message {
        return Message(DEFAULT_PREFIX + posIndex.toString())
    }

    fun generateMessages(count: Int, startPosIndex: Int = 0): List<Message> {
        return IntRange(startPosIndex, startPosIndex + count - 1).map {
            generateMessage(it)
        }
    }
}