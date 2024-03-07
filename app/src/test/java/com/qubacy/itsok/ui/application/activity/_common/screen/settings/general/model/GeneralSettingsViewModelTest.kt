package com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model

import androidx.lifecycle.SavedStateHandle
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.StatefulViewModelTest
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state.GeneralSettingsUiState

class GeneralSettingsViewModelTest(

) : StatefulViewModelTest<GeneralSettingsUiState, GeneralSettingsViewModel>() {
    override fun createViewModel(
        savedStateHandle: SavedStateHandle,
        errorDataRepository: ErrorDataRepository
    ): GeneralSettingsViewModel {
        return GeneralSettingsViewModel(savedStateHandle, errorDataRepository)
    }
}