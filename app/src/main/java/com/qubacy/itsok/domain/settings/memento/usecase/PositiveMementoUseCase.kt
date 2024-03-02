package com.qubacy.itsok.domain.settings.memento.usecase

import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.data.memento.model.toMemento
import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import com.qubacy.itsok.domain.settings.memento.usecase.result.GetMementoesDomainResult
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
}