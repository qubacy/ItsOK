package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state

import com.qubacy.itsok._common.error.Error
import java.io.Serializable

abstract class BaseUiState(
    var error: Error? = null,
    var isLoading: Boolean = false
) : Serializable {
    abstract fun copy(): BaseUiState
}