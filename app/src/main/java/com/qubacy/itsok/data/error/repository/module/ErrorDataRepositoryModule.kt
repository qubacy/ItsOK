package com.qubacy.itsok.data.error.repository.module

import android.content.Context
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.ui.application.ItsOkApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ErrorDataRepositoryModule {
    @Provides
    fun provideErrorDataRepository(
        @ApplicationContext context: Context
    ): ErrorDataRepository {
        return ErrorDataRepository((context as ItsOkApplication).db.errorDao())
    }
}