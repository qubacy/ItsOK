package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import com.qubacy.itsok.databinding.FragmentPositiveMementoesBinding
import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.domain.settings.memento.model.toUIMemento
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.util.extensional.getNavigationResult
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.BusinessFragment
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data.UIMemento
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor._common.mode.MementoEditorMode
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor.result.MementoEditorResult
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.adapter.PositiveMementoListAdapter
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.adapter.PositiveMementoListAdapterCallback
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.helper.PositiveMementoItemHelper
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.AddMementoUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.SetMementoesUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.UpdateMementoUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.PositiveMementoesUiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PositiveMementoesFragment(

) : BusinessFragment<PositiveMementoesUiState, PositiveMementoesViewModel>(),
    PositiveMementoListAdapterCallback
{
    companion object {
        const val TAG = "PositiveMementoesFrgmnt"
    }

    @Inject
    @PositiveMementoesViewModelFactoryQualifier
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val mModel: PositiveMementoesViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private lateinit var mBinding: FragmentPositiveMementoesBinding
    private lateinit var mAdapter: PositiveMementoListAdapter

    private var mMementoEditorLiveData: MutableLiveData<MementoEditorResult?>? = null

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
            setOnClickListener { onComposeMementoButtonClicked() }
        }

        mMementoEditorLiveData = getNavigationResult()

        mMementoEditorLiveData?.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            onMementoEditorResultGotten(it)

            mMementoEditorLiveData?.value = null
        }
    }

    private fun onMementoEditorResultGotten(editorResult: MementoEditorResult) {
        when (editorResult.mode) {
            MementoEditorMode.CREATOR -> createMemento(editorResult.memento)
            MementoEditorMode.EDITOR -> updateMemento(editorResult.memento)
        }
    }

    override fun runInitWithUiState(uiState: PositiveMementoesUiState) {
        super.runInitWithUiState(uiState)

        setMementoes(uiState.mementoes)
    }

    private fun onComposeMementoButtonClicked() {
        val action = PositiveMementoesFragmentDirections
            .actionPositiveMementoesFragmentToMementoEditorDialogFragment(
                MementoEditorMode.CREATOR, null)

        Navigation.findNavController(requireView()).navigate(action)
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

    override fun onResume() {
        super.onResume()

        if (mModel.uiState.mementoes.isEmpty()) initMementoes()
    }

    private fun initMementoes() {
        mModel.getMementoes()
    }

    private fun setMementoes(mementoes: List<Memento>) {
        val uiMementoes = resolveMementoes(mementoes)

        mAdapter.setMementoes(uiMementoes)
    }

    override fun processUiOperation(uiOperation: UiOperation): Boolean {
        if (super.processUiOperation(uiOperation)) return true

        when (uiOperation::class) {
            SetMementoesUiOperation::class ->
                processSetMementoesUiOperation(uiOperation as SetMementoesUiOperation)
            AddMementoUiOperation::class ->
                processAddMementoUiOperation(uiOperation as AddMementoUiOperation)
            UpdateMementoUiOperation::class ->
                processUpdateMementoUiOperation(uiOperation as UpdateMementoUiOperation)
            else -> return false
        }

        return true
    }

    private fun processSetMementoesUiOperation(
        mementoesOperation: SetMementoesUiOperation
    ) {
        setMementoes(mementoesOperation.mementoes)
    }

    private fun processAddMementoUiOperation(
        mementoOperation: AddMementoUiOperation
    ) {
        addMemento(mementoOperation.memento)
    }

    private fun addMemento(memento: Memento) {
        val resolvedMemento = resolveMemento(memento)

        mAdapter.addMemento(resolvedMemento)
    }

    private fun processUpdateMementoUiOperation(
        mementoOperation: UpdateMementoUiOperation
    ) {
        val resolvedMemento = resolveMemento(mementoOperation.memento)

        mAdapter.updateMemento(resolvedMemento, mementoOperation.index)
    }

    private fun resolveMementoes(mementoes: List<Memento>): List<UIMemento> {
        return mementoes.map { resolveMemento(it) }
    }

    private fun resolveMemento(memento: Memento): UIMemento {
        return memento.toUIMemento(requireContext())
    }

    override fun onMementoClicked(id: Long) {
        val memento = mModel.getMementoById(id)
        val action = PositiveMementoesFragmentDirections
            .actionPositiveMementoesFragmentToMementoEditorDialogFragment(
                mode = MementoEditorMode.EDITOR, memento = memento)

        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun onMementoRemoved(mementoId: Long) {
        mModel.removeMemento(mementoId)
    }

    private fun createMemento(memento: Memento) {
        mModel.createMemento(memento)
    }

    private fun updateMemento(memento: Memento) {
        mModel.updateMemento(memento)
    }
}