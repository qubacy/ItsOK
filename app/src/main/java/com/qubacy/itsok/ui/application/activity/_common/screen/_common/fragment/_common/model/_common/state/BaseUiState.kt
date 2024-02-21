package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state

import com.qubacy.itsok._common.error.Error

abstract class BaseUiState(
    var error: Error? = null
) {
    abstract fun copy(): BaseUiState
}