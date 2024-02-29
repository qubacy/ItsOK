package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.module

import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain.settings.memento.usecase.PositiveMementoUseCase
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModelFactory
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModelFactoryQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object PositiveMementoesViewModelModule {
    @Provides
    @PositiveMementoesViewModelFactoryQualifier
    fun providePositiveMementoesViewModelFactory(
        errorDataRepository: ErrorDataRepository,
        positiveMementoesUseCase: PositiveMementoUseCase
    ): ViewModelProvider.Factory {
        return PositiveMementoesViewModelFactory(
            errorDataRepository, positiveMementoesUseCase)
    }
}