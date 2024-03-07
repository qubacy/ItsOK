package com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state

import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.state.TestUiState

class TestGeneralSettingsUiState(
    error: Error? = null
) : GeneralSettingsUiState(error), TestUiState {

}