package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult
import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.domain.settings.memento.usecase.PositiveMementoUseCase
import com.qubacy.itsok.domain.settings.memento.usecase.result.CreateMementoDomainResult
import com.qubacy.itsok.domain.settings.memento.usecase.result.GetMementoesDomainResult
import com.qubacy.itsok.domain.settings.memento.usecase.result.UpdateMementoDomainResult
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.BusinessViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.AddMementoUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.SetMementoesUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.UpdateMementoUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.PositiveMementoesUiState
import javax.inject.Qualifier

open class PositiveMementoesViewModel(
    mSavedStateHandle: SavedStateHandle,
    mErrorDataRepository: ErrorDataRepository,
    private val mPositiveMementoUseCase: PositiveMementoUseCase
) : BusinessViewModel<PositiveMementoesUiState>(
    mSavedStateHandle, mErrorDataRepository, mPositiveMementoUseCase
) {
    override fun generateDefaultUiState(): PositiveMementoesUiState {
        return PositiveMementoesUiState()
    }

    open fun getMementoes() {
        changeLoadingState(true)

        mPositiveMementoUseCase.getMementoes()
    }

    open fun getMementoById(id: Long): Memento {
        return mUiState.mementoes.find { it.id == id }!!
    }

    open fun createMemento(memento: Memento) {
        mPositiveMementoUseCase.createMemento(memento)
    }

    open fun updateMemento(memento: Memento) {
        mPositiveMementoUseCase.updateMemento(memento)
    }

    open fun removeMemento(mementoId: Long) {
        mPositiveMementoUseCase.removeMemento(mementoId)
    }

    override fun processDomainResultFlow(domainResult: DomainResult): UiOperation? {
        val uiOperation = super.processDomainResultFlow(domainResult)

        if (uiOperation != null) return uiOperation

        return when (domainResult::class) {
            GetMementoesDomainResult::class ->
                processGetMementoesDomainResult(domainResult as GetMementoesDomainResult)
            CreateMementoDomainResult::class ->
                processCreateMementoDomainResult(domainResult as CreateMementoDomainResult)
            UpdateMementoDomainResult::class ->
                processUpdateMementoDomainResult(domainResult as UpdateMementoDomainResult)
            else -> null
        }
    }

    private fun processGetMementoesDomainResult(
        mementoesResult: GetMementoesDomainResult
    ): UiOperation {
        changeLoadingState(false)

        mUiState.mementoes = mementoesResult.mementoes

        return SetMementoesUiOperation(mementoesResult.mementoes)
    }

    private fun processCreateMementoDomainResult(
        mementoResult: CreateMementoDomainResult
    ): UiOperation {
        mUiState.mementoes = mUiState.mementoes.plus(mementoResult.memento)

        return AddMementoUiOperation(mementoResult.memento)
    }

    private fun processUpdateMementoDomainResult(
        mementoResult: UpdateMementoDomainResult
    ): UiOperation {
        val memento = mementoResult.memento
        val mementoIndex = mUiState.mementoes.indexOfFirst { it.id == memento.id }
        val updatedMementoes = mUiState.mementoes.toMutableList()

        updatedMementoes[mementoIndex] = memento

        mUiState.mementoes = updatedMementoes

        return UpdateMementoUiOperation(memento, mementoIndex)
    }
}

@Qualifier
annotation class PositiveMementoesViewModelFactoryQualifier

class PositiveMementoesViewModelFactory(
    private val mErrorDataRepository: ErrorDataRepository,
    private val mPositiveMementoUseCase: PositiveMementoUseCase
): AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (!modelClass.isAssignableFrom(PositiveMementoesViewModel::class.java))
            throw IllegalArgumentException()

        return PositiveMementoesViewModel(handle, mErrorDataRepository, mPositiveMementoUseCase) as T
    }
}