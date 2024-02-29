package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain.settings.memento.usecase.PositiveMementoUseCase
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.BusinessViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.PositiveMementoesUiState
import javax.inject.Qualifier

class PositiveMementoesViewModel(
    mSavedStateHandle: SavedStateHandle,
    mErrorDataRepository: ErrorDataRepository,
    private val mPositiveMementoUseCase: PositiveMementoUseCase
) : BusinessViewModel<PositiveMementoesUiState>(
    mSavedStateHandle, mErrorDataRepository, mPositiveMementoUseCase
) {
    override val mUiState: PositiveMementoesUiState = PositiveMementoesUiState()

}

@Qualifier
annotation class PositiveMementoesViewModelFactoryQualifier

class PositiveMementoesViewModelFactory(
    private val mErrorDataRepository: ErrorDataRepository,
    private val mPositiveMementoUseCase: PositiveMementoUseCase
): AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (!modelClass.isAssignableFrom(PositiveMementoesViewModel::class.java))
            throw IllegalArgumentException()

        return PositiveMementoesViewModel(handle, mErrorDataRepository, mPositiveMementoUseCase) as T
    }
}