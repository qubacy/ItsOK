package com.qubacy.itsok.domain.settings.memento.model._test.usecase

import com.qubacy.itsok.domain.settings.memento.model.Memento

object MementoGeneratorUtil {
    const val DEFAULT_TEXT_PREFIX = "test memento"

    fun generateMemento(id: Long? = null, text: String? = DEFAULT_TEXT_PREFIX): Memento {
        return Memento(id, text)
    }

    fun generateMementoes(
        count: Int,
        generateId: Boolean = true,
        idShift: Long = 0,
        textPrefix: String = DEFAULT_TEXT_PREFIX
    ): List<Memento> {
        return IntRange(0, count - 1)
            .map {
                val id = if (generateId) it.toLong() + idShift else null

                generateMemento(id, textPrefix + id.toString())
            }
    }
}