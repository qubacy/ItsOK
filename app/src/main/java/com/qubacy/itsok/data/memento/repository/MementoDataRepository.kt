package com.qubacy.itsok.data.memento.repository

import com.qubacy.itsok.data._common.repository.DataRepository
import com.qubacy.itsok.data.memento.model.DataMemento
import com.qubacy.itsok.data.memento.model.toMementoEntity
import com.qubacy.itsok.data.memento.repository.source.local.LocalMementoDataSource
import com.qubacy.itsok.data.memento.repository.source.local.entity.toDataMemento
import kotlin.random.Random

class MementoDataRepository(
    private val mLocalMementoDataSource: LocalMementoDataSource
) : DataRepository {
    fun getMementoById(mementoId: Long): DataMemento {
        return mLocalMementoDataSource.getMementoById(mementoId)!!.toDataMemento()
    }

    fun getRandomMemento(): DataMemento? {
        val mementoes = mLocalMementoDataSource.getMementoes()

        if (mementoes.isEmpty()) return null
        if (mementoes.size < 2) return mementoes.first().toDataMemento()

        val randomMementoIndex =
            Random(System.currentTimeMillis()).nextInt(0, mementoes.size)

        return mementoes[randomMementoIndex].toDataMemento()
    }

    fun addMemento(memento: DataMemento) {
        mLocalMementoDataSource.insertMemento(memento.toMementoEntity())
    }

    fun updateMemento(memento: DataMemento) {
        mLocalMementoDataSource.updateMemento(memento.toMementoEntity())
    }

    fun deleteMemento(memento: DataMemento) {
        mLocalMementoDataSource.deleteMemento(memento.toMementoEntity())
    }
}