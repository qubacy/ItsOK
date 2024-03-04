package com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model

import androidx.lifecycle.SavedStateHandle
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModelTest
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state.GeneralSettingsUiState

class GeneralSettingsViewModelTest(

) : BaseViewModelTest<GeneralSettingsUiState, GeneralSettingsViewModel>() {
    override fun createViewModel(
        savedStateHandle: SavedStateHandle,
        errorDataRepository: ErrorDataRepository
    ): GeneralSettingsViewModel {
        return GeneralSettingsViewModel(savedStateHandle, errorDataRepository)
    }
}