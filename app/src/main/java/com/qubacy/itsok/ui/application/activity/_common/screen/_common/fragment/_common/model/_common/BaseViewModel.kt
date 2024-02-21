package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok._common.error.type._common.ErrorType
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import com.qubacy.itsok.domain._common.usecase._common.result.SuccessfulResult
import com.qubacy.itsok.domain._common.usecase._common.result._common.Result
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState

abstract class BaseViewModel<UiStateType: BaseUiState>(
    protected val mSavedStateHandle: SavedStateHandle,
    private val mUseCase: UseCase
) : ViewModel() {
    companion object {
        const val UI_STATE_KEY = "uiState"
    }

    protected abstract var mUiState: UiStateType
    open val uiState: UiStateType get() = mUiState.copy() as UiStateType

    init {
        mUseCase.setCoroutineScope(viewModelScope)
    }

    open fun retrieveError(errorType: ErrorType): LiveData<Result<Error>> {
        return mUseCase.retrieveError(errorType).map {
            val error = (it as SuccessfulResult<Error>).data

            mUiState.error = error

            it
        }
    }
}