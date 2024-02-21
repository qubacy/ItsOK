package com.qubacy.itsok.data.memento.repository.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.qubacy.itsok.data._common.repository.source._common.DataSource
import com.qubacy.itsok.data.memento.repository.source.local.entity.MementoEntity

@Dao
interface LocalMementoDataSource : DataSource {
    @Query(
        "SELECT * " +
        "FROM ${MementoEntity.TABLE_NAME} " +
        "WHERE ${MementoEntity.ID_PROP_NAME} = :id"
    )
    fun getMementoById(id: Long): MementoEntity?

    @Query(
        "SELECT * " +
        "FROM ${MementoEntity.TABLE_NAME}"
    )
    fun getMementoes(): List<MementoEntity>

    @Insert
    fun insertMemento(memento: MementoEntity)

    @Update
    fun updateMemento(memento: MementoEntity)

    @Delete
    fun deleteMemento(memento: MementoEntity)
}