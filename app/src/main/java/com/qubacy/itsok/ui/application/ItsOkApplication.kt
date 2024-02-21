package com.qubacy.itsok.ui.application

import android.app.Application
import androidx.room.Room
import com.qubacy.itsok.data._common.repository.source.local.database.Database
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ItsOkApplication : Application() {
    private lateinit var mDB: Database
    val db get() = mDB

    override fun onCreate() {
        super.onCreate()

        mDB = Room.databaseBuilder(
            this, Database::class.java, Database.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .createFromAsset(Database.DATABASE_NAME)
            .build()
    }
}