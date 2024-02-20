package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model.state.BaseUiState

abstract class BaseViewModel<UiStateType: BaseUiState>(

) : ViewModel() {
    protected var mUiState: MutableLiveData<UiStateType> = MutableLiveData()
    /**
     * The UI State mechanics work like the following:
     * 1. ViewModel receives a signal from the UI Controller class (it's Fragment for our case).
     *    This signal is conveyed to the Domain layer, and the UI Controller gets a LiveData obj.
     *    to obtain the future result;
     * 2. After the Domain layer processing is finished, the results are MAPPED by calling
     *    .map() method at the beginning. At this stage, mUiState is updated and the result-related
     *    LiveData is;
     * 3. After data recovery (caused by Config. changes, etc.), the UI Controller gets the retained
     *    UI State from uiState field;
     */
    val uiState: LiveData<UiStateType> get() = mUiState
}