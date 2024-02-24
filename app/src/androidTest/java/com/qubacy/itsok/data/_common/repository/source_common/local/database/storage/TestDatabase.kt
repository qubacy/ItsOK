package com.qubacy.itsok.data._common.repository.source_common.local.database.storage

import android.content.Context
import androidx.room.Room
import com.qubacy.itsok.data._common.repository.source.local.database.Database

object TestDatabase {
    private var mDB: Database? = null

    fun getDatabase(context: Context): Database {
        if (mDB == null) {
            mDB = Room.databaseBuilder(
                context,
                Database::class.java,
                Database.DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }

        return mDB!!
    }
}