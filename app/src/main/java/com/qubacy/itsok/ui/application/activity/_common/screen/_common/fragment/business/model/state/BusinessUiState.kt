package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.state

import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.state.BaseUiState

abstract class BusinessUiState(
    var isLoading: Boolean = false,
    error: Error? = null
) : BaseUiState(error) {

}