package com.qubacy.itsok.domain.settings.memento.model._test.util

import com.qubacy.itsok.domain.settings.memento.model.Memento

object MementoUtilGenerator {
    const val DEFAULT_PREFIX = "test memento "

    fun generateMemento(id: Long = 0): Memento {
        return Memento(id, DEFAULT_PREFIX + id.toString())
    }

    fun generateMementoes(count: Int, idShift: Int = 0): List<Memento> {
        return IntRange(idShift, idShift + count - 1).map {
            val id = (it + idShift).toLong()

            generateMemento(id)
        }
    }
}