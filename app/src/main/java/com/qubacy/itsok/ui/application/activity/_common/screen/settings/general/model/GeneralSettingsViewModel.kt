package com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.StatefulViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state.GeneralSettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Qualifier

@HiltViewModel
open class GeneralSettingsViewModel @Inject constructor(
    mSavedStateHandle: SavedStateHandle,
    mErrorDataRepository: ErrorDataRepository
) : StatefulViewModel<GeneralSettingsUiState>(mSavedStateHandle, mErrorDataRepository) {
    override fun generateDefaultUiState(): GeneralSettingsUiState {
        return GeneralSettingsUiState()
    }
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