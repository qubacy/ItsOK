package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business

import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.error.ErrorUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.loading.SetLoadingStateUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.BusinessViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.state.BusinessUiState
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.StatefulFragment

abstract class BusinessFragment<
    UiStateType : BusinessUiState,
    ViewModelType : BusinessViewModel<UiStateType>
>() : StatefulFragment<UiStateType, ViewModelType>() {
    companion object {
        const val TAG = "BusinessFragment"
    }

    protected override fun runInitWithUiState(uiState: UiStateType) {
        super.runInitWithUiState(uiState)

        if (uiState.isLoading) setLoadingState(true)
    }

    protected override fun processUiOperation(uiOperation: UiOperation): Boolean {
        if (super.processUiOperation(uiOperation)) return true

        when (uiOperation::class) {
            ErrorUiOperation::class -> processErrorOperation(uiOperation as ErrorUiOperation)
            SetLoadingStateUiOperation::class ->
                processSetLoadingOperation(uiOperation as SetLoadingStateUiOperation)

            else -> return false
        }

        return true
    }

    private fun processErrorOperation(errorOperation: ErrorUiOperation) {
        onErrorOccurred(errorOperation.error)
    }

    protected open fun processSetLoadingOperation(loadingOperation: SetLoadingStateUiOperation) {}

    protected open fun setLoadingState(isLoading: Boolean) {}
}