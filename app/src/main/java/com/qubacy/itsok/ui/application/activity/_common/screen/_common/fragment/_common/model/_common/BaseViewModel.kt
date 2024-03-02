package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.itsok._common.error.type._common.ErrorType
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.error.ErrorUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiStateType: BaseUiState>(
    protected val mSavedStateHandle: SavedStateHandle,
    protected val mErrorDataRepository: ErrorDataRepository
) : ViewModel() {
    companion object {
        const val UI_STATE_KEY = "uiState"

        const val TAG = "BaseViewModel"
    }

    protected val mUiOperationFlow = MutableSharedFlow<UiOperation>()
    open val uiOperationFlow: Flow<UiOperation> = mUiOperationFlow

    protected var mUiState: UiStateType
    open val uiState: UiStateType get() = mUiState.copy() as UiStateType

    init {
        mUiState = mSavedStateHandle[UI_STATE_KEY] ?: generateDefaultUiState()
    }

    override fun onCleared() {
        mSavedStateHandle[UI_STATE_KEY] = mUiState

        super.onCleared()
    }

    protected abstract fun generateDefaultUiState() : UiStateType

    open fun retrieveError(errorType: ErrorType) {
        viewModelScope.launch(Dispatchers.IO) {
            val error = mErrorDataRepository.getError(errorType.id)

            mUiOperationFlow.emit(ErrorUiOperation(error))
        }
    }

    open fun absorbCurrentError() {
        mUiState.error = null
    }
}