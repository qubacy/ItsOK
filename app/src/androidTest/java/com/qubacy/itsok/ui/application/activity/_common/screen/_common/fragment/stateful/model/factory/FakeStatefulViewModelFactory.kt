package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.StatefulViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.state.BaseUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import org.mockito.Mockito

abstract class FakeStatefulViewModelFactory<UiStateType : BaseUiState> : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelMock = Mockito.mock(modelClass) as StatefulViewModel<UiStateType>

        Mockito.`when`(viewModelMock.uiOperationFlow).thenReturn(MutableSharedFlow())

        return viewModelMock as T
    }
}