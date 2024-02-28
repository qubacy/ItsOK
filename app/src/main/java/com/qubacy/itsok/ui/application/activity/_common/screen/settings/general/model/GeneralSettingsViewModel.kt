package com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state.GeneralSettingsUiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Qualifier

class GeneralSettingsViewModel(
    mSavedStateHandle: SavedStateHandle,
    mErrorDataRepository: ErrorDataRepository
) : BaseViewModel<GeneralSettingsUiState>(mSavedStateHandle, mErrorDataRepository) {
    override val uiOperationFlow: Flow<UiOperation> = mUiOperationFlow
    override val mUiState: GeneralSettingsUiState = GeneralSettingsUiState()
}

@Qualifier
annotation class GeneralSettingsViewModelFactoryQualifier

class GeneralSettingsViewModelFactory(
    private val mErrorDataRepository: ErrorDataRepository
): AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (!modelClass.isAssignableFrom(GeneralSettingsViewModel::class.java))
            throw IllegalArgumentException()

        return GeneralSettingsViewModel(handle, mErrorDataRepository) as T
    }
}