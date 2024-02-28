package com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state

import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState

class GeneralSettingsUiState(
    error: Error? = null
) : BaseUiState(error) {
    override fun copy(): GeneralSettingsUiState {
        return GeneralSettingsUiState(error?.copy())
    }
}