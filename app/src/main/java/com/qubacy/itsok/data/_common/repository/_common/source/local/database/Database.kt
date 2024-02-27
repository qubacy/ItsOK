package com.qubacy.itsok.data._common.repository._common.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.qubacy.itsok.data.answer.repository.source.local.LocalAnswerDataSource
import com.qubacy.itsok.data.answer.repository.source.local.entity.AnswerEntity
import com.qubacy.itsok.data.error.repository.source.local.LocalErrorDataSource
import com.qubacy.itsok.data.error.repository.source.local.model.ErrorEntity
import com.qubacy.itsok.data.memento.repository.source.local.LocalMementoDataSource
import com.qubacy.itsok.data.memento.repository.source.local.entity.MementoEntity

@Database(
    entities = [ErrorEntity::class, AnswerEntity::class, MementoEntity::class],
    version = 2
)
abstract class Database : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "ok.db"
    }

    abstract fun errorDao(): LocalErrorDataSource
    abstract fun answerDao(): LocalAnswerDataSource
    abstract fun mementoDao(): LocalMementoDataSource
}