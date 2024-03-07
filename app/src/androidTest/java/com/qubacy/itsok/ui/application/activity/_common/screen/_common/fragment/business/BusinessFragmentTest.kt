package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business

import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.StatefulFragmentTest
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.state.TestUiState
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.BusinessViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.state.BusinessUiState

abstract class BusinessFragmentTest<
    UiStateType : BusinessUiState,
    TestUiStateType : TestUiState,
    ViewModelType : BusinessViewModel<UiStateType>,
    FragmentType : BusinessFragment<UiStateType, ViewModelType>
>(

) : StatefulFragmentTest<UiStateType, TestUiStateType, ViewModelType, FragmentType>() {

}