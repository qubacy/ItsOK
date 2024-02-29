package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state

import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.state.BusinessUiState

class PositiveMementoesUiState(

) : BusinessUiState() {
    override fun copy(): PositiveMementoesUiState {
        return PositiveMementoesUiState()
    }
}