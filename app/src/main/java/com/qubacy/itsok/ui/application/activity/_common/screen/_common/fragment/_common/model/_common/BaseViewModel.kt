package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.itsok._common.error.type._common.ErrorType
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult
import com.qubacy.itsok.domain._common.usecase._common.result.error.ErrorDomainResult
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.error.ErrorUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.loading.SetLoadingStateUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiStateType: BaseUiState>(
    protected val mSavedStateHandle: SavedStateHandle,
    private val mUseCase: UseCase
) : ViewModel() {
    companion object {
        const val UI_STATE_KEY = "uiState"
    }

    protected val mUiOperationFlow = MutableSharedFlow<UiOperation>()
    open val uiOperationFlow = merge(mUiOperationFlow,
        mUseCase.resultFlow.map { mapDomainResultFlow(it) })

    protected abstract val mUiState: UiStateType
    open val uiState: UiStateType get() = mUiState.copy() as UiStateType

    init {
        mUseCase.setCoroutineScope(viewModelScope)
    }

    open fun retrieveError(errorType: ErrorType) {
        return mUseCase.retrieveError(errorType)
    }

    open fun absorbCurrentError() {
        mUiState.error = null
    }

    private fun mapDomainResultFlow(domainResult: DomainResult): UiOperation {
        return processDomainResultFlow(domainResult) ?: throw IllegalStateException()
    }

    protected open fun processDomainResultFlow(domainResult: DomainResult): UiOperation? {
        return when (domainResult::class) {
            ErrorDomainResult::class -> processErrorDomainResult(domainResult as ErrorDomainResult)
            else -> null
        }
    }

    private fun processErrorDomainResult(errorResult: ErrorDomainResult): ErrorUiOperation {
        mUiState.error = errorResult.error

        return ErrorUiOperation(errorResult.error)
    }

    protected fun changeLoadingState(isLoading: Boolean) {
        mUiState.isLoading = isLoading

        viewModelScope.launch {
            mUiOperationFlow.emit(SetLoadingStateUiOperation(isLoading))
        }
    }
}