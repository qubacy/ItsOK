package com.qubacy.itsok.domain.settings.memento.usecase

import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import javax.inject.Inject

class PositiveMementoUseCase @Inject constructor(
    mErrorDataRepository: ErrorDataRepository,
    private val mMementoDataRepository: MementoDataRepository
) : UseCase(mErrorDataRepository) {

}