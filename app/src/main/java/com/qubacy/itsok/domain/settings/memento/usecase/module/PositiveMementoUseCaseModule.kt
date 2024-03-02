package com.qubacy.itsok.domain.settings.memento.usecase.module

import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.domain.settings.memento.usecase.PositiveMementoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PositiveMementoUseCaseModule {
    @Provides
    fun providePositiveMementoUseCase(
        errorDataRepository: ErrorDataRepository,
        mementoDataRepository: MementoDataRepository
    ): PositiveMementoUseCase {
        return PositiveMementoUseCase(errorDataRepository, mementoDataRepository)
    }
}