package com.qubacy.itsok.data.memento.repository

import com.qubacy.itsok.data._common.repository._common.DataRepository
import com.qubacy.itsok.data.memento.model.DataMemento
import com.qubacy.itsok.data.memento.model.toMementoEntity
import com.qubacy.itsok.data.memento.repository.source.local.LocalMementoDataSource
import com.qubacy.itsok.data.memento.repository.source.local.entity.toDataMemento
import javax.inject.Inject
import kotlin.random.Random

class MementoDataRepository @Inject constructor(
    private val mLocalMementoDataSource: LocalMementoDataSource
) : DataRepository {
    fun getMementoById(mementoId: Long): DataMemento {
        return mLocalMementoDataSource.getMementoById(mementoId)!!.toDataMemento()
    }

    fun getAllMementoes(): List<DataMemento> {
        return mLocalMementoDataSource.getMementoes().map { it.toDataMemento() }
    }

    fun getRandomMemento(): DataMemento? {
        val mementoes = mLocalMementoDataSource.getMementoes()

        if (mementoes.isEmpty()) return null
        if (mementoes.size < 2) return mementoes.first().toDataMemento()

        val randomMementoIndex =
            Random(System.currentTimeMillis()).nextInt(0, mementoes.size)

        return mementoes[randomMementoIndex].toDataMemento()
    }

    fun addMemento(memento: DataMemento): DataMemento {
        val insertedMementoId = mLocalMementoDataSource
            .insertMemento(memento.toMementoEntity())

        return mLocalMementoDataSource.getMementoById(insertedMementoId)!!.toDataMemento()
    }

    fun updateMemento(memento: DataMemento): DataMemento {
        mLocalMementoDataSource.updateMemento(memento.toMementoEntity())

        return mLocalMementoDataSource.getMementoById(memento.id!!)!!.toDataMemento()
    }

    fun deleteMementoById(id: Long) {
        mLocalMementoDataSource.deleteMementoById(id)
    }
}