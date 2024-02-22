package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import org.mockito.Mockito

abstract class FakeBaseViewModelFactory<UiStateType : BaseUiState> : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelMock = Mockito.mock(modelClass) as BaseViewModel<UiStateType>

        Mockito.`when`(viewModelMock.uiOperationFlow).thenReturn(MutableSharedFlow())

        return viewModelMock as T
    }
}