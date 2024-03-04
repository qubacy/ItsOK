package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data._test.util

import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data.UIMemento

object UIMementoUtilGenerator {
    const val DEFAULT_ID = 0L
    const val DEFAULT_TEXT_PREFIX = "test memento"

    fun generateUIMemento(id: Long = DEFAULT_ID, text: String? = DEFAULT_TEXT_PREFIX): UIMemento {
        return UIMemento(id, text)
    }

    fun generateUIMementoes(
        count: Int,
        textPrefix: String = DEFAULT_TEXT_PREFIX
    ): MutableList<UIMemento> {
        return IntRange(0, count - 1)
            .map {
                generateUIMemento(it.toLong(), textPrefix + it.toString())
            }.toMutableList()
    }
}