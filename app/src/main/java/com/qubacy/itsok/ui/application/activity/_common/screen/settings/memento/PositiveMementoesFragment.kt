package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.databinding.FragmentPositiveMementoesBinding
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.BusinessFragment
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.PositiveMementoesUiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PositiveMementoesFragment(

) : BusinessFragment<PositiveMementoesUiState, PositiveMementoesViewModel>() {
    @Inject
    @PositiveMementoesViewModelFactoryQualifier
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val mModel: PositiveMementoesViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private lateinit var mBinding: FragmentPositiveMementoesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPositiveMementoesBinding
            .inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun adjustViewToInsets(insets: Insets) {
        super.adjustViewToInsets(insets)

        mBinding.fragmentGeneralSettingsTopBarWrapper.apply {
            updatePadding(top = insets.top)
        }
    }
}