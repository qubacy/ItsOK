package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.qubacy.itsok.R
import com.qubacy.itsok.databinding.FragmentPositiveMementoesBinding
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.BusinessFragment
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data.UIMemento
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.adapter.PositiveMementoListAdapter
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.adapter.PositiveMementoListAdapterCallback
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.helper.PositiveMementoItemHelper
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.PositiveMementoesUiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PositiveMementoesFragment(

) : BusinessFragment<PositiveMementoesUiState, PositiveMementoesViewModel>(),
    PositiveMementoListAdapterCallback
{
    @Inject
    @PositiveMementoesViewModelFactoryQualifier
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val mModel: PositiveMementoesViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private lateinit var mBinding: FragmentPositiveMementoesBinding
    private lateinit var mAdapter: PositiveMementoListAdapter

    private var mDefaultComposeMementoButtonMarginBottom: Int = 0

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

        mAdapter = PositiveMementoListAdapter(this)

        mBinding.fragmentPositiveMementoesList.apply {
            adapter = mAdapter

             ItemTouchHelper(PositiveMementoItemHelper(mAdapter))
                 .attachToRecyclerView(this)
        }
        mBinding.fragmentPositiveMementoesButtonComposeMemento.apply {
            onComposeMementoButtonClicked()
        }

        // todo: remove:
        val image = ResourcesCompat.getDrawable(
            resources, R.drawable.ic_launcher_background, requireContext().theme)
        mAdapter.setMementoes(listOf(
            UIMemento(0, "test text"),
            UIMemento(1, "test text", image),
            UIMemento(2, image = image)
        ))
    }

    private fun onComposeMementoButtonClicked() {
        // todo: implement..


    }

    override fun adjustViewToInsets(insets: Insets) {
        super.adjustViewToInsets(insets)

        mBinding.fragmentPositiveMementoesTopBarWrapper.apply {
            updatePadding(top = insets.top)
        }
        mBinding.fragmentPositiveMementoesButtonComposeMemento.apply {
            updateLayoutParams<ConstraintLayout.LayoutParams> {
                if (mDefaultComposeMementoButtonMarginBottom == 0)
                    mDefaultComposeMementoButtonMarginBottom = bottomMargin

                bottomMargin = mDefaultComposeMementoButtonMarginBottom + insets.bottom
            }
        }
    }

    override fun onMementoRemoved(id: Long) {
        // todo: implement..


    }
}