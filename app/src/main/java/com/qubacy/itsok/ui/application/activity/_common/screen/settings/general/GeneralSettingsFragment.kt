package com.qubacy.itsok.ui.application.activity._common.screen.settings.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.qubacy.itsok.R
import com.qubacy.itsok.databinding.FragmentGeneralSettingsBinding
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.StatefulFragment
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.GeneralSettingsViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.GeneralSettingsViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state.GeneralSettingsUiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GeneralSettingsFragment : StatefulFragment<
    GeneralSettingsUiState, GeneralSettingsViewModel
>() {
    @Inject
    @GeneralSettingsViewModelFactoryQualifier
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val mModel: GeneralSettingsViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private lateinit var mBinding: FragmentGeneralSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentGeneralSettingsBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.fragmentGeneralSettingsMainSectionPositiveMementoes.getButton().setOnClickListener {
            onPositiveMementoesSettingClicked()
        }
    }

    private fun onPositiveMementoesSettingClicked() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_generalSettingsFragment_to_positiveMementoesFragment)
    }

    override fun adjustViewToInsets(insets: Insets) {
        super.adjustViewToInsets(insets)

        mBinding.fragmentGeneralSettingsTopBarWrapper.apply {
            updatePadding(top = insets.top)
        }
    }
}