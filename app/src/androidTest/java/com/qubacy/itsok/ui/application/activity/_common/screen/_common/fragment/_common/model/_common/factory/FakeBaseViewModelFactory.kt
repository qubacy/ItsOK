package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.factory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok._common._test.util.mock.AnyMockUtil
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState
import org.mockito.Mockito

abstract class FakeBaseViewModelFactory<UiStateType : BaseUiState> : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelMock = Mockito.mock(modelClass) as BaseViewModel<UiStateType>

        Mockito.`when`(viewModelMock.retrieveError(AnyMockUtil.anyObject()))
            .thenReturn(MutableLiveData())

        return viewModelMock as T
    }
}