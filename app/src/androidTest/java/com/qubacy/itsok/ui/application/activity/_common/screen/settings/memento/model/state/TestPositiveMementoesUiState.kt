package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state

import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.state.TestUiState

class TestPositiveMementoesUiState(
    isLoading: Boolean = false,
    error: Error? = null,
    mementoes: List<Memento> = listOf(),
    var mementoToCreate: Memento? = null,
    var mementoToUpdate: Memento? = null,
    var mementoToRemoveId: Long? = null
) : PositiveMementoesUiState(isLoading, error, mementoes), TestUiState {

}