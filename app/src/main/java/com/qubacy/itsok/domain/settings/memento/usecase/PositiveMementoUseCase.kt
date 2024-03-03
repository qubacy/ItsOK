package com.qubacy.itsok.domain.settings.memento.usecase

import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.data.memento.model.toMemento
import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.domain.settings.memento.model.toDataMemento
import com.qubacy.itsok.domain.settings.memento.usecase.result.CreateMementoDomainResult
import com.qubacy.itsok.domain.settings.memento.usecase.result.GetMementoesDomainResult
import com.qubacy.itsok.domain.settings.memento.usecase.result.UpdateMementoDomainResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class PositiveMementoUseCase @Inject constructor(
    mErrorDataRepository: ErrorDataRepository,
    private val mMementoDataRepository: MementoDataRepository
) : UseCase(mErrorDataRepository) {
    fun getMementoes() {
        mCoroutineScope.launch(mCoroutineDispatcher) {
            val mementoes = mMementoDataRepository.getAllMementoes()
                .map { it.toMemento() }

            mResultFlow.emit(GetMementoesDomainResult(mementoes))
        }
    }

    fun createMemento(memento: Memento) {
        mCoroutineScope.launch(mCoroutineDispatcher) {
            val dataMemento = memento.toDataMemento()
            val addedMemento = mMementoDataRepository.addMemento(dataMemento).toMemento()

            mResultFlow.emit(CreateMementoDomainResult(addedMemento))
        }
    }

    fun updateMemento(memento: Memento) {
        mCoroutineScope.launch(mCoroutineDispatcher) {
            val dataMemento = memento.toDataMemento()
            val updatedMemento = mMementoDataRepository.updateMemento(dataMemento).toMemento()

            mResultFlow.emit(UpdateMementoDomainResult(updatedMemento))
        }
    }

    fun removeMemento(mementoId: Long) {
        mCoroutineScope.launch(mCoroutineDispatcher) {
            mMementoDataRepository.deleteMementoById(mementoId)
        }
    }
}