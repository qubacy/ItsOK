package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.business

import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.stateful.StatefulFragmentTest
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.BusinessFragment
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.BusinessViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.state.BusinessUiState

abstract class BusinessFragmentTest<
    UiStateType : BusinessUiState,
    ViewModelType : BusinessViewModel<UiStateType>,
    FragmentType : BusinessFragment<UiStateType, ViewModelType>
>(

) : StatefulFragmentTest<UiStateType, ViewModelType, FragmentType>() {

}