package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state

import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.state.BusinessUiState

class PositiveMementoesUiState(
    isLoading: Boolean = false,
    error: Error? = null,
    var mementoes: List<Memento> = listOf()
) : BusinessUiState(isLoading, error) {
    override fun copy(): PositiveMementoesUiState {
        return PositiveMementoesUiState(isLoading, error?.copy(), mementoes.toList())
    }
}