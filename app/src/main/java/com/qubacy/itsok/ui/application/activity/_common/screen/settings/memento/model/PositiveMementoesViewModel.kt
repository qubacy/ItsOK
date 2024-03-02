package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult
import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.domain.settings.memento.usecase.PositiveMementoUseCase
import com.qubacy.itsok.domain.settings.memento.usecase.result.GetMementoesDomainResult
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.BusinessViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.SetMementoesUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.PositiveMementoesUiState
import javax.inject.Qualifier

class PositiveMementoesViewModel(
    mSavedStateHandle: SavedStateHandle,
    mErrorDataRepository: ErrorDataRepository,
    private val mPositiveMementoUseCase: PositiveMementoUseCase
) : BusinessViewModel<PositiveMementoesUiState>(
    mSavedStateHandle, mErrorDataRepository, mPositiveMementoUseCase
) {
    override fun generateDefaultUiState(): PositiveMementoesUiState {
        return PositiveMementoesUiState()
    }

    fun getMementoes() {
        changeLoadingState(true)

        mPositiveMementoUseCase.getMementoes()
    }
    fun getMementoById(id: Long): Memento {
        return mUiState.mementoes.find { it.id == id }!!
    }

    override fun processDomainResultFlow(domainResult: DomainResult): UiOperation? {
        val uiOperation = super.processDomainResultFlow(domainResult)

        if (uiOperation != null) return uiOperation

        return when (domainResult::class) {
            GetMementoesDomainResult::class ->
                processGetMementoesDomainResult(domainResult as GetMementoesDomainResult)
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