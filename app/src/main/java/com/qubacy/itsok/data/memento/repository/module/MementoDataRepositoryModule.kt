package com.qubacy.itsok.data.memento.repository.module

import android.content.Context
import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.ui.application.ItsOkApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MementoDataRepositoryModule {
    @Provides
    fun provideMementoDataRepository(
        @ApplicationContext context: Context
    ): MementoDataRepository {
        return MementoDataRepository((context as ItsOkApplication).db.mementoDao())
    }
}