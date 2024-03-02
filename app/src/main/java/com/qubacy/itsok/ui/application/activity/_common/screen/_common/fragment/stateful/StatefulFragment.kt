package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful

import androidx.lifecycle.lifecycleScope
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.BaseFragment
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.error.ErrorUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class StatefulFragment<
    UiStateType : BaseUiState,
    ViewModelType : BaseViewModel<UiStateType>
>() : BaseFragment() {
    companion object {
        const val TAG = "StatefulFragment"
    }

    protected abstract val mModel: ViewModelType
    private var mOperationCollectionJob: Job? = null

    override fun onStop() {
        mOperationCollectionJob?.cancel()

        super.onStop()
    }

    override fun onStart() {
        super.onStart()

        startOperationCollection()
        initUiState(mModel.uiState)
    }

    private fun startOperationCollection() {
        mOperationCollectionJob = lifecycleScope.launch {
            mModel.uiOperationFlow.collect { processUiOperation(it) }
        }
    }

    private fun initUiState(uiState: UiStateType) {
        runInitWithUiState(uiState)
    }

    protected open fun runInitWithUiState(uiState: UiStateType) {
        if (uiState.error != null) onErrorOccurred(uiState.error!!)
    }

    protected open fun processUiOperation(uiOperation: UiOperation): Boolean {
        when (uiOperation::class) {
            ErrorUiOperation::class -> processErrorOperation(uiOperation as ErrorUiOperation)
            else -> return false
        }

        return true
    }

    private fun processErrorOperation(errorOperation: ErrorUiOperation) {
        onErrorOccurred(errorOperation.error)
    }

    override fun onErrorHandled() {
        mModel.absorbCurrentError()
    }
}
