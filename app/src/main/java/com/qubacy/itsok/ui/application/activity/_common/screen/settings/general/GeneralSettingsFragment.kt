package com.qubacy.itsok.ui.application.activity._common.screen.settings.general

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.BaseFragment
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.GeneralSettingsViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.GeneralSettingsViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state.GeneralSettingsUiState
import javax.inject.Inject

class GeneralSettingsFragment : BaseFragment<
    GeneralSettingsUiState, GeneralSettingsViewModel
>() {
    @Inject
    @GeneralSettingsViewModelFactoryQualifier
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val mModel: GeneralSettingsViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )


}